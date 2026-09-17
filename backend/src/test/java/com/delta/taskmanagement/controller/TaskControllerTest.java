package com.delta.taskmanagement.controller;

import com.delta.taskmanagement.dto.StatusUpdateRequest;
import com.delta.taskmanagement.dto.TaskCreateRequest;
import com.delta.taskmanagement.dto.TaskUpdateRequest;
import com.delta.taskmanagement.enums.Priority;
import com.delta.taskmanagement.enums.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- CREATE ---

    @Test
    public void testCreateTask() throws Exception {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setPriority(Priority.HIGH);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.version").value(0));
    }

    @Test
    public void testCreateTaskValidationFailure() throws Exception {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle(""); // blank title — invalid

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    // --- READ ---

    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void testFilterByStatus() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testFilterByPriority() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testSearchByKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .param("keyword", "project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    public void testGetTaskById() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- UPDATE ---

    @Test
    public void testUpdateTask() throws Exception {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Task");
        request.setDescription("Updated Description");
        request.setPriority(Priority.LOW);
        request.setVersion(0L);

        mockMvc.perform(put("/api/v1/tasks/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    public void testUpdateTaskOptimisticLockConflict() throws Exception {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Conflicting Update");
        request.setPriority(Priority.LOW);
        request.setVersion(99L); // stale version

        mockMvc.perform(put("/api/v1/tasks/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    // --- UPDATE STATUS ---

    @Test
    public void testUpdateTaskStatus() throws Exception {
        StatusUpdateRequest request = new StatusUpdateRequest();
        request.setStatus(TaskStatus.COMPLETED);
        request.setVersion(0L);

        mockMvc.perform(patch("/api/v1/tasks/3/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.version").value(1));
    }

    // --- DELETE ---

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/4"))
                .andExpect(status().isNoContent());

        // verify it's gone
        mockMvc.perform(get("/api/v1/tasks/4"))
                .andExpect(status().isNotFound());
    }
}
