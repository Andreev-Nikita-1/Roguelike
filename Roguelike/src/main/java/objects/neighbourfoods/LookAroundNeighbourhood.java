package objects.neighbourfoods;

import map.MapOfObjects;
import objects.MapObject;
import util.Coord;

public class LookAroundNeighbourhood extends MapObject {
    private Coord center;
    private int radius;
    private ViewNeighbourhood neighbourhood;

    public LookAroundNeighbourhood(MapOfObjects map, Coord center, int radius) {
        super(map);
        this.center = center;
        this.radius = radius;
        neighbourhood = new ViewNeighbourhood(map, center, radius);
    }

    @Override
    public LookAroundNeighbourhood attachToMap() {
        super.attachToMap();
        neighbourhood.attachToMap();
        return this;
    }

    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
        neighbourhood.deleteFromMap();
    }

    public boolean checkHeroLocation() {
        return neighbourhood.number(map.getHeroLocation(), 1, Coord::lInftyNorm) != -1;
    }
}
