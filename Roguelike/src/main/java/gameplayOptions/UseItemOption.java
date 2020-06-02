package gameplayOptions;

/**
 * Using item in inventory. num bitween 1 and 4
 */
public class UseItemOption extends GameplayOption {
    public int num;

    public UseItemOption(int num) {
        this.num = num;
    }
}
