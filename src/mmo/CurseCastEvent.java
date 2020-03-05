package mmo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurseCastEvent implements IEvent {
    final ICard curse;
    final CastCurseSource castCurseSource;
    final IMunchkin caster;
    final IMunchkin target;
}

// when player is cursed
// if castCurseSource == CastCurseSource.KICK_DOWN_DOOR
//   don't apply curse
//   send message
//   play sound effect, e.t.c.
// else
//   do nothing