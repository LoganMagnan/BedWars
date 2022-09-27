package rip.tilly.bedwars.utils;

public interface TtlHandler<E> {

    void onExpire(E element);

    long getTimestamp(E element);
}
