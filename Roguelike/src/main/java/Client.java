import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class Client {
    public ManagedChannel channel;
    public RoguelikeGrpc.RoguelikeBlockingStub blockingStub;

    public Client(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    public Client(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = RoguelikeGrpc.newBlockingStub(channel);
    }

    public int join(Model.JoinMessage message) {
        try {
            return blockingStub.join(message).getId();
        } catch (StatusRuntimeException e) {
            System.err.println(String.format("RPC join failed: %s", e.getStatus()));
            return -1;
        }
    }

    public void doHeroAction(Model.HeroAction action) {
        try {
            blockingStub.doHeroAction(action);
        } catch (StatusRuntimeException e) {
            System.err.println(String.format("RPC doHeroAction failed: %s", e.getStatus()));
        }
    }

    public ByteString getPixels(Model.GetPixelsMessage message) {
        try {
            return blockingStub.getPixels(message).getPixels();
        } catch (StatusRuntimeException e) {
            System.err.println(String.format("RPC getPixels failed: %s", e.getStatus()));
            return null;
        }
    }
}
