package mmo;

import com.google.common.collect.ImmutableList;

public class CurseHandler {
    void cast(final ICard curse, final IMunchkin target, final CastCurseSource castCurseSource) {
        final ImmutableList<CurseCastEvent> events = target.getEvents(CurseCastEvent.class);
        for (final CurseCastEvent event : events) {

        }
    }
}
