package fh.server.helpers;

import fh.server.entity.*;
import fh.server.entity.widget.Poll;
import fh.server.entity.widget.Widget;

public class Context {

    private String input;

    private Entity entity;

    private Artifact artifact;

    private Site site;

    private Page page;

    private Widget widget;

    private Poll poll;

    private Account principal;

    private Alias alias;

    private boolean locked;

    private Context() {

    }

    public static ContextBuilder build() {
        return new ContextBuilder();
    }

    public static class ContextBuilder {
        private final Context context = new Context();

        public ContextBuilder input(String input) {
            if (context.locked) throw new IllegalStateException();
            context.input = input;
            return this;
        }

        public ContextBuilder entity(Entity entity) {
            if (context.locked) throw new IllegalStateException();
            context.entity = entity;
            return this;
        }

        public ContextBuilder artifact(Artifact artifact) {
            if (context.locked) throw new IllegalStateException();
            context.artifact = artifact;
            return this;
        }

        public ContextBuilder site(Site site) {
            if (context.locked) throw new IllegalStateException();
            context.site = site;
            return this;
        }

        public ContextBuilder page(Page page) {
            if (context.locked) throw new IllegalStateException();
            context.page = page;
            return this;
        }

        public ContextBuilder widget(Widget widget) {
            if (context.locked) throw new IllegalStateException();
            context.widget = widget;
            return this;
        }

        public ContextBuilder poll(Poll poll) {
            if (context.locked) throw new IllegalStateException();
            context.poll = poll;
            return this;
        }

        public ContextBuilder principal(Account principal) {
            if (context.locked) throw new IllegalStateException();
            context.principal = principal;
            return this;
        }

        public ContextBuilder alias(Alias alias) {
            if (context.locked) throw new IllegalStateException();
            context.alias = alias;
            return this;
        }

        public Context dispatch() {
            context.locked = true;
            return context;
        }
    }


    public String getInput() {
        return input;
    }

    public Entity getEntity() {
        return entity;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public Site getSite() {
        return site;
    }

    public Page getPage() {
        return page;
    }

    public Widget getWidget() {
        return widget;
    }

    public Poll getPoll() {
        return poll;
    }

    public Account getPrincipal() {
        return principal;
    }

    public Alias getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return "Context{" +
                "input='" + input + '\'' +
                ", entity=" + entity +
                ", artifact=" + artifact +
                ", site=" + site +
                ", page=" + page +
                ", widget=" + widget +
                ", poll=" + poll +
                ", principal=" + principal +
                ", alias=" + alias +
                '}';
    }
}
