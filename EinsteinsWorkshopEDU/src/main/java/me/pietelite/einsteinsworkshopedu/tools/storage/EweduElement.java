package me.pietelite.einsteinsworkshopedu.tools.storage;

import org.spongepowered.api.text.Text;

public interface EweduElement {

    StorageLine toStorageLine();

    Text formatReadable(int id);

}
