package me.pietelite.einsteinsworkshopedu.tools.storage;

public interface EweduElementFactory<P extends EweduElement> {

    P construct(StorageLine line) throws IllegalArgumentException;

}
