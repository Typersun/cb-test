package savkin.ilya.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private int id;
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String inn;
    @NotEmpty
    private String address;
}
