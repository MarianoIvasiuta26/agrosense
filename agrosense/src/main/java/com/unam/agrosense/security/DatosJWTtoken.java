package com.unam.agrosense.security;

import com.unam.agrosense.model.usuario.Rol;

public record   DatosJWTtoken(
        String token,
        String rol
) {
}
