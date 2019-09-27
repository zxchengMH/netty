package com.example.nettyserver.config.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NettyHttpRequest
 * @Description TODO
 * @Author zxcheng - 1213328214@qq.com
 * @Date 2019/9/25 16:38
 * @Version 1.0
 */
public class NettyHttpRequest implements FullHttpRequest {

    private FullHttpRequest request;

    private Map<String, String> parameters;

    public NettyHttpRequest(FullHttpRequest request) {
        this.request = request;
        this.parameters = new HashMap<>();
        parametersDispose();
    }

    public String getContent(){
        String body = null;
        if(request.content().isReadable()){
            body = request.content().toString(CharsetUtil.UTF_8);
        }
        return body;
    }

    public Map<String, String> getParameters(){
        return this.parameters;
    }

    public String getParameter(String name){
        return this.parameters.get(name);
    }

    private void parametersDispose(){
        String url = request.uri();
        int f = url.indexOf("?");
        if(f < 0){
            return;
        }
        String pu = url.substring(f + 1);
        int flag = 1;
        while (flag > 0){
            flag = pu.indexOf("&");
            if(flag < 0){
                setParameter(pu);
                break;
            }
            String p = pu.substring(0, flag);
            setParameter(p);
            pu = pu.substring(flag + 1);
        }
    }

    private void setParameter(String p){
        int f = p.indexOf("=");
        if(f < 0){
            return;
        }
        String key = p.substring(0, f);
        String value = p.substring(f + 1);
        this.parameters.put(key, value);
    }

    @Override
    public FullHttpRequest copy() {
        return this.request.copy();
    }

    @Override
    public FullHttpRequest duplicate() {
        return this.request.duplicate();
    }

    @Override
    public FullHttpRequest retainedDuplicate() {
        return this.request.retainedDuplicate();
    }

    @Override
    public FullHttpRequest replace(ByteBuf byteBuf) {
        return this.request.replace(byteBuf);
    }

    @Override
    public FullHttpRequest retain(int i) {
        return this.request.retain(i);
    }

    @Override
    public FullHttpRequest retain() {
        return this.request.retain();
    }

    @Override
    public FullHttpRequest touch() {
        return this.request.touch();
    }

    @Override
    public FullHttpRequest touch(Object o) {
        return this.request.touch(o);
    }

    @Override
    public FullHttpRequest setProtocolVersion(HttpVersion httpVersion) {
        return this.request.setProtocolVersion(httpVersion);
    }

    @Override
    public FullHttpRequest setMethod(HttpMethod httpMethod) {
        return this.request.setMethod(httpMethod);
    }

    @Override
    public FullHttpRequest setUri(String s) {
        return this.request.setUri(s);
    }

    @Override
    public ByteBuf content() {
        return this.request.content();
    }

    @Override
    public HttpMethod getMethod() {
        return this.request.method();
    }

    @Override
    public HttpMethod method() {
        return this.request.method();
    }

    @Override
    public String getUri() {
        return this.request.uri();
    }

    @Override
    public String uri() {
        return this.request.uri();
    }

    @Override
    public HttpVersion getProtocolVersion() {
        return this.request.protocolVersion();
    }

    @Override
    public HttpVersion protocolVersion() {
        return this.request.protocolVersion();
    }

    @Override
    public HttpHeaders headers() {
        return this.request.headers();
    }

    @Override
    public HttpHeaders trailingHeaders() {
        return this.request.trailingHeaders();
    }

    @Override
    public DecoderResult getDecoderResult() {
        return this.request.decoderResult();
    }

    @Override
    public DecoderResult decoderResult() {
        return this.request.decoderResult();
    }

    @Override
    public void setDecoderResult(DecoderResult decoderResult) {

    }

    @Override
    public int refCnt() {
        return 0;
    }

    @Override
    public boolean release() {
        return false;
    }

    @Override
    public boolean release(int i) {
        return false;
    }
}
