package org.mule.tools.apikit;

public interface ApikitLogger
{ //TODO
    public void info(String message);
    public void error(String message);
    public void error(Object message, Throwable e);
    public void warn(String message);
    public void debug (Exception e);
}