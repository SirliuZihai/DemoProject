package com.zihai.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

@Plugin(name = "LogPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "reqId" })
public final class LogPatternConverter extends LogEventPatternConverter {
    /**
     * Singleton.
     */
    private static final LogPatternConverter INSTANCE =
            new LogPatternConverter();

    /**
     * Private constructor.
     */
    private LogPatternConverter() {
        super("Log", "reqId");
    }

    /**
     * Obtains an instance of NdcPatternConverter.
     *
     * @param options options, may be null.
     * @return instance of NdcPatternConverter.
     */
    public static LogPatternConverter newInstance(final String[] options) {
        return INSTANCE;
    }

    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        String req = event.getContextData().getValue("requestId");
        toAppendTo.append(req);
    }
}
