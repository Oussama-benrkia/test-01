package ma.realestate.properties.handler;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return new GraphQLError() {
            @Override
            public String getMessage() {
                return ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            }

            @Override
            public List<SourceLocation> getLocations() {
                return env.getField().getSourceLocation() != null
                        ? List.of(env.getField().getSourceLocation())
                        : null;
            }

            @Override
            public ErrorClassification getErrorType() {
                return null;
            }

            @Override
            public Map<String, Object> getExtensions() {
                return Map.of("errorType", ex.getClass().getSimpleName());
            }

            @Override
            public List<Object> getPath() {
                List<Object> path = env.getExecutionStepInfo().getPath().toList();
                return path != null ? path : List.of();
            }

            @Override
            public Map<String, Object> toSpecification() {
                Map<String, Object> spec = new HashMap<>();
                spec.put("message", getMessage());
                spec.put("locations", getLocations() != null ? getLocations() : List.of());
                spec.put("path", getPath());
                spec.put("extensions", getExtensions() != null ? getExtensions() : Map.of());
                return spec;
            }

            public Throwable getCause() {
                return ex;
            }
        };
    }
}
