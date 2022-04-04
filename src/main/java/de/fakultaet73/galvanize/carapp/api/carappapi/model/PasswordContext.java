package de.fakultaet73.galvanize.carapp.api.carappapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Setter
@Getter
public class PasswordContext {

    @NotNull
    private Long id;

    @NotNull @NotEmpty
    private String password;

}
