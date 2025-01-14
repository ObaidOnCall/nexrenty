package ma.crm.carental.integration;


import  org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.dtos.vehicule.BrandRequsetDto;



@AutoConfigureMockMvc
@Slf4j
class BrandControllerIntegrationTest extends BaseControllerTest {
    
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }


    @Test
    void saveUpdateDeleteBrands_shouldReturnCorrectOperations () throws Exception {

        BrandRequsetDto requestDto = new BrandRequsetDto();
        requestDto.setName("GOGO Brand");
        requestDto.setCountryOfOrigin("Test Country");
        requestDto.setParentCompany("Test Parent");
        requestDto.setWebsite("https://test.com");


        List<BrandRequsetDto> requestDtos = Arrays.asList(requestDto);

        String response = mockMvc.perform(
            prepareRequestWithOAuthToken(HttpMethod.POST, "/brands")
                .content(toJson(requestDtos))
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("GOGO Brand"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
        .andReturn().getResponse().getContentAsString();



        String id = JsonPath.read(response, "$[0].id").toString();

        log.debug("Id : {} ❗" , id);

        // Step 2: Retrieve the object (GET request)
        mockMvc.perform(
            prepareRequestWithOAuthToken(HttpMethod.GET, "/brands/" + id)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("GOGO Brand"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));


        // // Step 3: Update the object (PUT request)
        // requestDto.setName("Updated Brand");


        // mockMvc.perform(
        //     prepareRequestWithOAuthToken(HttpMethod.PUT, "/brands/" + id)
        //         .content(toJson(Arrays.asList(requestDto)))
        // )
        // .andExpect(MockMvcResultMatchers.status().isOk())
        // .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Updated Brand"));

        // // Step 4: Delete the object (DELETE request)

        // mockMvc.perform(
        //     prepareRequestWithOAuthToken(HttpMethod.DELETE, "/brands/" + id)
        // )
        // .andExpect(MockMvcResultMatchers.status().isNoContent());


        // // Verify that the object is deleted

        // mockMvc.perform(
        //     prepareRequestWithOAuthToken(HttpMethod.GET, "/brands/" + id)
        // )
        // .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
