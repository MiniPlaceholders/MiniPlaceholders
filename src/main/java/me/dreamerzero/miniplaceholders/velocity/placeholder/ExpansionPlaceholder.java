package me.dreamerzero.miniplaceholders.velocity.placeholder;

abstract class ExpansionPlaceholder<M> {
    private String name;
    private M function;
    protected ExpansionPlaceholder(String name, M function){
        this.name = name;
        this.function = function;
    }
    public String name() {
        return this.name;
    }

    public M get() {
        return this.function;
    }
}
