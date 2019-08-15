package me.pietelite.einsteinsworkshopedu.tools.storage;

import java.util.LinkedList;
import java.util.List;

public class StorageLineBuilder {

    private List<String> items;

    StorageLineBuilder() {
        items = new LinkedList<>();
    }

    public StorageLineBuilder addItem(String item) {
        items.add(StorageLine.removeDelimiters(item));
        return this;
    }

    public StorageLineBuilder addItem(double item) {
        items.add(String.valueOf(item));
        return this;
    }

    public StorageLineBuilder addItem(boolean item) {
        items.add(String.valueOf(item));
        return this;
    }

    public StorageLine build() {
        return new StorageLine(items);
    }

}
