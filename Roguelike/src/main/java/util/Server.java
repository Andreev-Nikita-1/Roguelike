package util;

import basicComponents.GameplayLogic;
import com.google.protobuf.ByteString;
import gameplayOptions.DirectedOption;
import io.grpc.ServerBuilder;

import java.io.IOException;

import static basicComponents.GameplayLogic.currentMap;

public class Server extends RoguelikeGrpc.RoguelikeImplBase {

    @Override
    public void join(Model.JoinMessage request,
                     io.grpc.stub.StreamObserver<Model.HeroId> responseObserver) {
        int new_hero_id = currentMap.addHero();
        responseObserver.onNext(Model.HeroId.newBuilder().setId(new_hero_id).build());
        responseObserver.onCompleted();
    }

    @Override
    public void doHeroAction(Model.HeroAction request,
                             io.grpc.stub.StreamObserver<Model.Nothing> responseObserver) {
        int id = request.getHeroId();
        Model.HeroAction.Type type = request.getType();
        if (type == Model.HeroAction.Type.NOTHING) {
        } else if (type == Model.HeroAction.Type.INTERACT) {
            currentMap.heroObjects[id].interactWith();
        } else if (type == Model.HeroAction.Type.DIRECTED_ACTION) {
            Model.HeroAction.Action action = request.getAction();
            Model.HeroAction.Direction direction = request.getDirection();
            long eventTime = request.getEventTime();
            DirectedOption.Action action1 = null;
            switch (action) {
                case WALK:
                    action1 = DirectedOption.Action.WALK;
                    break;
                case RUN:
                    action1 = DirectedOption.Action.RUN;
                    break;
                case ATTACK:
                    action1 = DirectedOption.Action.ATTACK;
                    break;
            }
            Direction direction1 = null;
            switch (direction) {
                case LEFT:
                    direction1 = Direction.LEFT;
                    break;
                case RIGHT:
                    direction1 = Direction.RIGHT;
                    break;
                case DOWN:
                    direction1 = Direction.DOWN;
                    break;
                case UP:
                    direction1 = Direction.UP;
                    break;
            }
            if (action1 == DirectedOption.Action.ATTACK) {
                currentMap.heroObjects[id].makeAttack(new DirectedOption(action1, direction1), eventTime);
            } else {
                currentMap.heroObjects[id].makeMovement(new DirectedOption(action1, direction1), eventTime);
            }

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
        ByteString pixels = ByteString.copyFrom(GameplayLogic.currentMapRenderer
                .drawMapForClient(hero_id, terminalSizeX, terminalSizeY).getBytes());
        Model.Pixels answer = Model.Pixels.newBuilder().setPixels(pixels).build();
        responseObserver.onNext(answer);
        responseObserver.onCompleted();
    }

    // call this to run the server
    public static io.grpc.Server run(int port) throws IOException, InterruptedException {
        Server serverImpl = new Server(); // TODO maybe add state to util.Server
        io.grpc.Server server = ServerBuilder.forPort(port).addService(serverImpl).build();
        server.start();

        // Wait somehow

        // server.shutdownNow();
        // server.awaitTermination();
        return server;
    }
}
