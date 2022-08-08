package com.orangetv.cloud.videostore.vo.gateway;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

@Data
@NoArgsConstructor
public class RouteDefinition {
    private String id;
    private List<PredicateDefinition> predicates = new ArrayList<>();
    private List<FilterDefinition> filters = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();
    private String uri;
    private int order;

    static final String GENERATED_NAME_PREFIX = "_genkey_";

    static String generateName(int i) {
        return GENERATED_NAME_PREFIX + i;
    }

    @Data
    @NoArgsConstructor
    public static class PredicateDefinition {

        private String name;

        private Map<String, String> args = new LinkedHashMap<>();

        public PredicateDefinition(String text) {
            int eqIdx = text.indexOf('=');
            if (eqIdx <= 0) {
                throw new IllegalArgumentException(
                        "Unable to parse PredicateDefinition text '" + text + "'" + ", must be of the form name=value");
            }
            setName(text.substring(0, eqIdx));

            String[] args = tokenizeToStringArray(text.substring(eqIdx + 1), ",");

            for (int i = 0; i < args.length; i++) {
                this.args.put(generateName(i), args[i]);
            }
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PredicateDefinition that = (PredicateDefinition) o;
            return Objects.equals(name, that.name) && Objects.equals(args, that.args);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, args);
        }

        @Override
        public String toString() {
            return "PredicateDefinition{" + "name='" + name + '\'' + ", args=" + args + '}';
        }
    }

    @Data
    @NoArgsConstructor
    public static class FilterDefinition {

        private String name;

        private Map<String, String> args = new LinkedHashMap<>();

        public FilterDefinition(String text) {
            int eqIdx = text.indexOf('=');
            if (eqIdx <= 0) {
                setName(text);
                return;
            }
            setName(text.substring(0, eqIdx));

            String[] args = tokenizeToStringArray(text.substring(eqIdx + 1), ",");

            for (int i = 0; i < args.length; i++) {
                this.args.put(generateName(i), args[i]);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FilterDefinition that = (FilterDefinition) o;
            return Objects.equals(name, that.name) && Objects.equals(args, that.args);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, args);
        }

        @Override
        public String toString() {
            return "FilterDefinition{" + "name='" + name + '\'' + ", args=" + args + '}';
        }
    }
}
