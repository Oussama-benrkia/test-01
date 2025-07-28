package ma.realestate.properties.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusResponse<Res> {
    private Res response;
    private Boolean changed;

    public static <Res> ChangeStatusResponse<Res> buildResponse(Res response, Boolean changed) {
        return ChangeStatusResponse.<Res>builder()
                .response(response)
                .changed(changed)
                .build();
    }
}
