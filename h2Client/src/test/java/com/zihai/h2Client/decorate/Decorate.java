package com.zihai.h2Client.decorate;

public class Decorate extends Component{
    Component component;

    public void SetComponent(Component component){
        this.component = component;
    }

    @Override
    public void operate() {
        if(component!=null)
            component.operate();
    }

    public static void main(String[] args) {
        Decorate decorate = new DecorateB();
        decorate.SetComponent(new ComponentA());
        decorate.operate();
    }
}
class DecorateA extends Decorate {
    private String states;
    @Override
    public void operate(){
        super.operate();
        System.out.println("add decorateA");
    }
}
class DecorateB extends Decorate {
    private String states;
    @Override
    public void operate(){
        super.operate();
        System.out.println("add decorateB");
    }
}