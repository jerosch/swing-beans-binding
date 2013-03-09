package de.opitzconsulting.jbinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JLabel;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Binding {

    private JComponent component;
    private ReflectionReadAccessor accessor;
    private Object model;

    public Binding(JComponent component) {
        this.component = component;
    }

    public <T> T to(T object) {
        model = object;

        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(object.getClass().getClassLoader());
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback(new MethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                setAccessor(new ReflectionReadAccessor(method));
                return null;
            }
        });

        return (T) enhancer.create();
    }

    protected void setAccessor(ReflectionReadAccessor reflectionReadAccessor) {
        try {
            ((JLabel) component).setText(String.valueOf(reflectionReadAccessor.readFrom(model)));
        } catch (Exception e) {
            e.printStackTrace();
            ((JLabel) component).setText(e.getMessage());
        }
    }

    private static class ReflectionReadAccessor {

        private Method method;

        public ReflectionReadAccessor(Method method) {
            this.method = method;
        }

        public Object readFrom(Object object) throws IllegalAccessException, InvocationTargetException {
            return method.invoke(object);
        }

    }

}
