package me.moonways.bridgenet.api.module;

public class ModuleData {

	private final Module module;
	private final String id, name, version;

	public ModuleData(Module module, String id, String name, String version) {
		this.module = module;
		this.id = id;
		this.name = name;
		this.version = version;
	}

	public Module getModule() {
		return module;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}
}