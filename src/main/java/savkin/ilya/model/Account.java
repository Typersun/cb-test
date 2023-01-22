package savkin.ilya.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import savkin.ilya.model.enums.AccountCurrency;
import savkin.ilya.model.enums.AccountStatus;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Integer id;
    @NotNull
    private String number;
    private BigDecimal amount;
    @NotNull
    private AccountStatus status;
    @NotNull
    private String BIC;
    @NotNull
    private AccountCurrency currency;
    @NotNull
    private Integer clientId;
    @NotNull
    private String clientFullName;
}

