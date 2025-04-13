package com.lms.course_service.unit_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lms.course_service.dtos.filters.ModuleFilterParams;
import com.lms.course_service.dtos.requests.AddResourceToModuleRequest;
import com.lms.course_service.dtos.requests.CreateModuleRequest;
import com.lms.course_service.dtos.responses.ModuleResponse;
import com.lms.course_service.dtos.responses.ServiceResult;
import com.lms.course_service.entities.CourseEntity;
import com.lms.course_service.entities.ModuleEntity;
import com.lms.course_service.repositories.CourseRepository;
import com.lms.course_service.repositories.ModuleRepository;
import com.lms.course_service.services.ModuleService;
import com.lms.shared.events.ResourceExistsResponseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ModuleService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ModuleServiceTest {
    @MockitoBean
    private CourseRepository courseRepository;

    @MockitoBean
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleService moduleService;

    @MockitoBean
    private ReplyingKafkaTemplate<String, Object, ResourceExistsResponseEvent> replyingKafkaTemplate;


    @Test
    @DisplayName("Test createModule(CreateModuleRequest); then return Data CourseId is 'null'")
    void testCreateModule_thenReturnDataCourseIdIsNull() {
        // Arrange
        CourseEntity course = new CourseEntity();
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructorId(1L);
        course.setMaxStudents(3);
        course.setModules(new ArrayList<>());
        course.setTitle("Dr");

        ModuleEntity moduleEntity = new ModuleEntity();
        moduleEntity.setCourse(course);
        moduleEntity.setDescription("The characteristics of someone or something");
        moduleEntity.setId(1L);
        moduleEntity.setResourceIds(new ArrayList<>());
        moduleEntity.setTitle("Dr");
        when(moduleRepository.save(Mockito.any())).thenReturn(moduleEntity);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setDescription("The characteristics of someone or something");
        courseEntity.setId(1L);
        courseEntity.setInstructorId(1L);
        courseEntity.setMaxStudents(3);
        courseEntity.setModules(new ArrayList<>());
        courseEntity.setTitle("Dr");
        Optional<CourseEntity> ofResult = Optional.of(courseEntity);
        when(courseRepository.findCourseEntitiesById(Mockito.any())).thenReturn(ofResult);

        // Act
        ServiceResult<ModuleResponse> actualCreateModuleResult = moduleService.createModule(new CreateModuleRequest());

        // Assert
        verify(courseRepository).findCourseEntitiesById(isNull());
        verify(moduleRepository).save(isA(ModuleEntity.class));
        ModuleResponse data = actualCreateModuleResult.getData();
        assertNull(data.getCourseId());
        assertNull(data.getId());
        assertNull(data.getDescription());
        assertNull(data.getTitle());
        assertNull(actualCreateModuleResult.getMessageError());
        assertNull(data.getResources());
        assertEquals(HttpStatus.CREATED, actualCreateModuleResult.getHttpStatus());
        assertTrue(actualCreateModuleResult.isSuccess());
    }


    @Test
    @DisplayName("Test createModule(CreateModuleRequest); then return MessageError is 'Course not found for ID: null'")
    void testCreateModule_thenReturnMessageErrorIsCourseNotFoundForIdNull() {
        // Arrange
        Optional<CourseEntity> emptyResult = Optional.empty();
        when(courseRepository.findCourseEntitiesById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act
        ServiceResult<ModuleResponse> actualCreateModuleResult = moduleService.createModule(new CreateModuleRequest());

        // Assert
        verify(courseRepository).findCourseEntitiesById(isNull());
        assertEquals("Course not found for ID: null", actualCreateModuleResult.getMessageError());
        assertNull(actualCreateModuleResult.getData());
        assertEquals(HttpStatus.NOT_FOUND, actualCreateModuleResult.getHttpStatus());
        assertFalse(actualCreateModuleResult.isSuccess());
    }


    @Test
    @DisplayName("Test getModules(ModuleFilterParams); given CourseEntity() Description is 'Description'; then return size is two")
    void testGetModules_givenCourseEntityDescriptionIsDescription_thenReturnSizeIsTwo() {
        // Arrange
        CourseEntity course = new CourseEntity();
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructorId(1L);
        course.setMaxStudents(3);
        course.setModules(new ArrayList<>());
        course.setTitle("Dr");

        ModuleEntity moduleEntity = new ModuleEntity();
        moduleEntity.setCourse(course);
        moduleEntity.setDescription("The characteristics of someone or something");
        moduleEntity.setId(1L);
        moduleEntity.setResourceIds(new ArrayList<>());
        moduleEntity.setTitle("Dr");

        CourseEntity course2 = new CourseEntity();
        course2.setDescription("Description");
        course2.setId(2L);
        course2.setInstructorId(2L);
        course2.setMaxStudents(1);
        course2.setModules(new ArrayList<>());
        course2.setTitle("Mr");

        ModuleEntity moduleEntity2 = new ModuleEntity();
        moduleEntity2.setCourse(course2);
        moduleEntity2.setDescription("Description");
        moduleEntity2.setId(2L);
        moduleEntity2.setResourceIds(new ArrayList<>());
        moduleEntity2.setTitle("Mr");

        ArrayList<ModuleEntity> content = new ArrayList<>();
        content.add(moduleEntity2);
        content.add(moduleEntity);
        when(moduleRepository.findModulesEntitiesByFilters(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));

        ModuleFilterParams filterParams = new ModuleFilterParams();
        filterParams.setCourseID(1L);
        filterParams.setModuleId(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<ModuleResponse> actualModules = moduleService.getModules(filterParams);

        // Assert
        verify(moduleRepository).findModulesEntitiesByFilters(eq(1L), eq(1L), isA(Pageable.class));
        assertEquals(2, actualModules.size());
        ModuleResponse getResult = actualModules.get(0);
        assertEquals("Description", getResult.getDescription());
        ModuleResponse getResult2 = actualModules.get(1);
        assertEquals("Dr", getResult2.getTitle());
        assertEquals("Mr", getResult.getTitle());
        assertEquals("The characteristics of someone or something", getResult2.getDescription());
        assertNull(getResult2.getCourseId());
        assertEquals(1L, getResult2.getId().longValue());
        assertEquals(2L, getResult.getId().longValue());
        assertTrue(getResult2.getResources().isEmpty());
    }


    @Test
    @DisplayName("Test getModules(ModuleFilterParams); given ModuleEntity() Course is CourseEntity(); then return size is one")
    void testGetModules_givenModuleEntityCourseIsCourseEntity_thenReturnSizeIsOne() {
        // Arrange
        CourseEntity course = new CourseEntity();
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructorId(1L);
        course.setMaxStudents(3);
        course.setModules(new ArrayList<>());
        course.setTitle("Dr");

        ModuleEntity moduleEntity = new ModuleEntity();
        moduleEntity.setCourse(course);
        moduleEntity.setDescription("The characteristics of someone or something");
        moduleEntity.setId(1L);
        moduleEntity.setResourceIds(new ArrayList<>());
        moduleEntity.setTitle("Dr");

        ArrayList<ModuleEntity> content = new ArrayList<>();
        content.add(moduleEntity);
        when(moduleRepository.findModulesEntitiesByFilters(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));

        ModuleFilterParams filterParams = new ModuleFilterParams();
        filterParams.setCourseID(1L);
        filterParams.setModuleId(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<ModuleResponse> actualModules = moduleService.getModules(filterParams);

        // Assert
        verify(moduleRepository).findModulesEntitiesByFilters(eq(1L), eq(1L), isA(Pageable.class));
        assertEquals(1, actualModules.size());
        ModuleResponse getResult = actualModules.getFirst();
        assertEquals("Dr", getResult.getTitle());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals(1L, getResult.getId().longValue());
    }


    @Test
    @DisplayName("Test getModules(ModuleFilterParams); given ModuleEntity getId() return one; then calls getDescription()")
    void testGetModules_givenModuleEntityGetIdReturnOne_thenCallsGetDescription() {
        // Arrange
        CourseEntity course = new CourseEntity();
        course.setDescription("The characteristics of someone or something");
        course.setId(1L);
        course.setInstructorId(1L);
        course.setMaxStudents(3);
        course.setModules(new ArrayList<>());
        course.setTitle("Dr");
        ModuleEntity moduleEntity = mock(ModuleEntity.class);
        when(moduleEntity.getId()).thenReturn(1L);
        when(moduleEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(moduleEntity.getTitle()).thenReturn("Dr");
        when(moduleEntity.getResourceIds()).thenReturn(new ArrayList<>());
        doNothing().when(moduleEntity).setCourse(Mockito.any());
        doNothing().when(moduleEntity).setDescription(Mockito.any());
        doNothing().when(moduleEntity).setId(Mockito.any());
        doNothing().when(moduleEntity).setResourceIds(Mockito.any());
        doNothing().when(moduleEntity).setTitle(Mockito.any());
        moduleEntity.setCourse(course);
        moduleEntity.setDescription("The characteristics of someone or something");
        moduleEntity.setId(1L);
        moduleEntity.setResourceIds(new ArrayList<>());
        moduleEntity.setTitle("Dr");

        ArrayList<ModuleEntity> content = new ArrayList<>();
        content.add(moduleEntity);
        when(moduleRepository.findModulesEntitiesByFilters(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(new PageImpl<>(content));

        ModuleFilterParams filterParams = new ModuleFilterParams();
        filterParams.setCourseID(1L);
        filterParams.setModuleId(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<ModuleResponse> actualModules = moduleService.getModules(filterParams);

        // Assert
        verify(moduleEntity).getDescription();
        verify(moduleEntity).getId();
        verify(moduleEntity).getResourceIds();
        verify(moduleEntity).getTitle();
        verify(moduleEntity).setCourse(isA(CourseEntity.class));
        verify(moduleEntity).setDescription(eq("The characteristics of someone or something"));
        verify(moduleEntity).setId(eq(1L));
        verify(moduleEntity).setResourceIds(isA(List.class));
        verify(moduleEntity).setTitle(eq("Dr"));
        verify(moduleRepository).findModulesEntitiesByFilters(eq(1L), eq(1L), isA(Pageable.class));
        assertEquals(1, actualModules.size());
        ModuleResponse getResult = actualModules.getFirst();
        assertEquals("Dr", getResult.getTitle());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals(1L, getResult.getId().longValue());
    }


    @Test
    @DisplayName("Test getModules(ModuleFilterParams); then return Empty")
    void testGetModules_thenReturnEmpty() {
        // Arrange
        when(moduleRepository.findModulesEntitiesByFilters(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        ModuleFilterParams filterParams = new ModuleFilterParams();
        filterParams.setCourseID(1L);
        filterParams.setModuleId(1L);
        filterParams.setPage(1);
        filterParams.setSize(3);

        // Act
        List<ModuleResponse> actualModules = moduleService.getModules(filterParams);

        // Assert
        verify(moduleRepository).findModulesEntitiesByFilters(eq(1L), eq(1L), isA(Pageable.class));
        assertTrue(actualModules.isEmpty());
    }


    @Test
    @DisplayName("Test addResourceToModule(AddResourceToModuleRequest); then return MessageError is 'Module not found for ID: 1'")
    void testAddResourceToModule_thenReturnMessageErrorIsModuleNotFoundForId1() {
        // Arrange
        ModuleRepository moduleRepository = mock(ModuleRepository.class);
        when(moduleRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        ProducerFactory<String, Object> producerFactory = mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        DefaultKafkaConsumerFactory<? super String, ? super ResourceExistsResponseEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(
                new HashMap<>());
        ModuleService moduleService = new ModuleService(moduleRepository, mock(CourseRepository.class),
                new ReplyingKafkaTemplate<>(producerFactory, new ConcurrentMessageListenerContainer<>(consumerFactory,
                        new ContainerProperties(Pattern.compile(".*\\.txt")))));
        AddResourceToModuleRequest request = AddResourceToModuleRequest.builder().moduleId(1L).resourceId(1L).build();

        // Act
        ServiceResult<ModuleResponse> actualAddResourceToModuleResult = moduleService.addResourceToModule(request);

        // Assert
        verify(moduleRepository).existsById(eq(1L));
        verify(producerFactory).transactionCapable();
        assertEquals("Module not found for ID: 1", actualAddResourceToModuleResult.getMessageError());
        assertNull(actualAddResourceToModuleResult.getData());
        assertEquals(HttpStatus.NOT_FOUND, actualAddResourceToModuleResult.getHttpStatus());
        assertFalse(actualAddResourceToModuleResult.isSuccess());
    }
}
