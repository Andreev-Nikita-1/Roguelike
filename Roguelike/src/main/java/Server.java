import com.google.protobuf.ByteString;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Server extends RoguelikeGrpc.RoguelikeImplBase {

    @Override
    public void join(Model.JoinMessage request,
                     io.grpc.stub.StreamObserver<Model.HeroId> responseObserver) {
        // some code TODO
        int new_hero_id = 42; // TODO
        responseObserver.onNext(Model.HeroId.newBuilder().setId(new_hero_id).build());
        responseObserver.onCompleted();
    }

    @Override
    public void doHeroAction(Model.HeroAction request,
                             io.grpc.stub.StreamObserver<Model.Nothing> responseObserver) {
        int hero_id = request.getHeroId();
        Model.HeroAction.Type type = request.getType();
        if (type == Model.HeroAction.Type.NOTHING) {
            // TODO some code
        } else if (type == Model.HeroAction.Type.INTERACT) {
            // TODO some code
        } else if (type == Model.HeroAction.Type.DIRECTED_ACTION) {
            Model.HeroAction.Action action = request.getAction();
            Model.HeroAction.Direction direction = request.getDirection();
            long eventTime = request.getEventTime();
            // TODO some code
        }
        responseObserver.onNext(Model.Nothing.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPixels(Model.GetPixelsMessage request,
                          io.grpc.stub.StreamObserver<Model.Pixels> responseObserver) {
        int hero_id = request.getHeroId();
        int terminalSizeX = request.getTerminalSizeX();
        int terminalSizeY = request.getTerminalSizeY();
        // TODO some code
        ByteString pixels = ByteString.copyFrom(new String("Nikita").getBytes()); // TODO change
        Model.Pixels answer = Model.Pixels.newBuilder().setPixels(pixels).build();
        responseObserver.onNext(answer);
        responseObserver.onCompleted();
    }

    // call this to run the server
    public static io.grpc.Server run(int port) throws IOException, InterruptedException {
        Server serverImpl = new Server(); // TODO maybe add state to Server
        io.grpc.Server server = ServerBuilder.forPort(port).addService(serverImpl).build();
        server.start();

        // Wait somehow

        // server.shutdownNow();
        // server.awaitTermination();
        return server;
    }
}
