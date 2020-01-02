package com.ysma.ppt.service.groovy;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.script.*;
import java.util.Map;

import static javax.script.ScriptContext.ENGINE_SCOPE;
import static javax.script.ScriptContext.GLOBAL_SCOPE;

/**
 * @author ysma groovy 脚本引擎
 */
@Slf4j
@Service
public class GroovyScriptEngine implements ScriptEngine {

    private final ApplicationContext applicationContext;

    //groovy脚本引擎
    private GroovyScriptEngineImpl groovyScriptEngine = (GroovyScriptEngineImpl)new ScriptEngineManager().getEngineByName("groovy");

    /**构造器注入*/
    public GroovyScriptEngine(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object execute(String script, Map<String, Object> bindsMap) {
        if (StringUtils.isEmpty(script)) {
            log.error("GroovyScriptEngine.execute 脚本为空");
            return null;
        }

        try {
            //1.编译脚本
            CompiledScript compiledScript = groovyScriptEngine.compile(script);

            //2.创建上下文
            ScriptContext scriptContext = createScriptContext(bindsMap);

            //3.执行脚本
            return compiledScript.eval(scriptContext);
        } catch (ScriptException e) {
            log.error("GroovyScriptEngine.execute 脚本编译/执行异常,script:{}", script, e);
        }

        return null;
    }

    /**创建脚本上下文*/
    private ScriptContext createScriptContext(Map<String, Object> bindsMap) {
        //1.Bindings构建
        Bindings binds = new SimpleBindings();
        binds.putAll(bindsMap);
        binds.putIfAbsent("applicationContext", applicationContext);

        //2.scriptContext上下文获取
        ScriptContext scriptContext = groovyScriptEngine.getContext();

        //3.定制新的上下文
        ScriptContext customScriptCtx = new SimpleScriptContext();
        customScriptCtx.setBindings(binds, GLOBAL_SCOPE);
        customScriptCtx.setBindings(scriptContext.getBindings(ENGINE_SCOPE), ENGINE_SCOPE);
        customScriptCtx.setWriter(scriptContext.getWriter());
        customScriptCtx.setReader(scriptContext.getReader());
        customScriptCtx.setErrorWriter(scriptContext.getErrorWriter());
        return customScriptCtx;
    }

}
