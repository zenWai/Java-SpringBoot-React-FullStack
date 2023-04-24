package com.presa.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest {
    @Test
    void mapRow() throws SQLException {
        //given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        //no annotation to mock
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("Jakline");
        when(resultSet.getString("email")).thenReturn("jakline@super.com");
        //when
        Customer customer = customerRowMapper.mapRow(resultSet, 1);
        //then
        Customer expected = new Customer(
                1, "Jakline", "jakline@super.com",19
        );
        assertThat(customer).isEqualTo(expected);
    }
}