package me.pietelite.einsteinsworkshopedu.nickname;

import java.util.Optional;
import java.util.function.Function;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.google.common.reflect.TypeToken;

public class Nickname implements Value<Text> {

	private static final Text DEFAULT_NICKNAME = Text.of("NULL");
	private Text nickname;
	private final Player player;
	
	public Nickname(Player player, Text nickname) {
		this.nickname = nickname;
		this.player = player;
	}
	
	@Override
	public Text get() {
		return nickname;
	}

	@Override
	public boolean exists() {
		return nickname != null;
	}

	@Override
	public Text getDefault() {
		return DEFAULT_NICKNAME;
	}

	@Override
	public Optional<Text> getDirect() {
		return Optional.of(nickname);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Key<? extends BaseValue<Text>> getKey() {
//		try {
//			return Key.builder()
//					.type((TypeToken<Value<Text>>) TypeToken.of(this.getClass().getInterfaces()[0]))
//					.name(nickname.toPlain())
//					.id("ewedu:nickname" + player.getUniqueId().toString() + ":" + nickname.toPlain())
//					.query(DataQuery.of("DisplayName"))
//					.build();
//		} catch (ClassCastException e2) {
//			e2.printStackTrace();
//		}
//		return null;
		return (Key<? extends BaseValue<Text>>) player.getDisplayNameData().getKeys().toArray()[0];
	}

	@Override
	public Value<Text> set(Text value) {
		this.nickname = value;
		return this;
	}

	@Override
	public Value<Text> transform(Function<Text, Text> function) {
		this.nickname = function.apply(nickname);
		return this;
	}

	@Override
	public ImmutableValue<Text> asImmutable() {
		return this.asImmutable();
	}

}
