package utils;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

public class ProtoUtil {

    public static String proto2json(GeneratedMessageV3 messageV3) throws InvalidProtocolBufferException {
        return proto2json(messageV3, null);
    }

    public static String proto2json(GeneratedMessageV3 messageV3, Descriptors.Descriptor... descriptors) throws InvalidProtocolBufferException {
        JsonFormat.Printer printer = JsonFormat.printer();
        if (null != descriptors && descriptors.length > 0) {
            JsonFormat.TypeRegistry.Builder builder = JsonFormat.TypeRegistry.newBuilder();
            for (int i = 0; i < descriptors.length; i++) {
                builder.add(descriptors[i]);
            }
            JsonFormat.TypeRegistry build = builder.build();
            printer = printer.usingTypeRegistry(build);
        }
        return printer.includingDefaultValueFields().omittingInsignificantWhitespace().print(messageV3);
    }

    public static void json2proto(String json, Builder builder) throws InvalidProtocolBufferException {
        JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
    }
}
