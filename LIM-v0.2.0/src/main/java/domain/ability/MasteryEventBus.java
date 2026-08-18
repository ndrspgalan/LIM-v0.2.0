package domain.ability;

import domain.ability.event.MasteryEvent;
import java.util.*;
import java.util.function.Consumer;

/** Bus sin duplicaciones: una pasiva desbloqueada registra un único consumidor por nombre. */
public final class MasteryEventBus {
 private final Map<String,Subscription> subscriptions=new LinkedHashMap<>();
 public synchronized boolean register(String passiveId,Class<? extends MasteryEvent> eventType,Consumer<MasteryEvent> handler){Objects.requireNonNull(passiveId);Objects.requireNonNull(eventType);Objects.requireNonNull(handler);return subscriptions.putIfAbsent(passiveId,new Subscription(eventType,handler))==null;}
 public synchronized boolean unregister(String passiveId){return subscriptions.remove(passiveId)!=null;}
 public synchronized boolean isRegistered(String passiveId){return subscriptions.containsKey(passiveId);}
 public synchronized Set<String> registeredPassiveIds(){return Set.copyOf(subscriptions.keySet());}
 public synchronized void publish(MasteryEvent event){for(Subscription s:List.copyOf(subscriptions.values()))if(s.eventType().isInstance(event))s.handler().accept(event);}
 public synchronized void clear(){subscriptions.clear();}
 private record Subscription(Class<? extends MasteryEvent> eventType,Consumer<MasteryEvent> handler){}
}
