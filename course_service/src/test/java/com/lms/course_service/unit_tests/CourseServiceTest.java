//package com.lms.course_service.unit_tests;
//
//import static org.mockito.Mockito.any;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.ArgumentMatchers.isA;
//import static org.mockito.ArgumentMatchers.isNull;
//import static org.mockito.Mockito.anyInt;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.lms.course_service.dtos.filters.CourseFilterParams;
//import com.lms.course_service.dtos.requests.AssignStudentToCourseRequest;
//import com.lms.course_service.dtos.requests.CreateCourseRequest;
//import com.lms.course_service.dtos.responses.CourseResponse;
//import com.lms.course_service.dtos.responses.ServiceResult;
//import com.lms.course_service.entities.CourseEntity;
//import com.lms.course_service.repositories.CourseRepository;
//import com.lms.course_service.repositories.CourseStudentRepository;
//import com.lms.course_service.services.CourseService;
//import com.lms.shared.events.UserExistsResponseEvent;
//
//import java.util.*;
//import java.util.regex.Pattern;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
//import org.springframework.kafka.requestreply.RequestReplyFuture;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.aot.DisabledInAotMode;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@ContextConfiguration(classes = {CourseService.class})
//@ExtendWith(SpringExtension.class)
//@DisabledInAotMode
//class CourseServiceTest {
//    @MockitoBean
//    private CourseRepository courseRepository;
//
//    @Autowired
//    private CourseService courseService;
//
//    @MockitoBean
//    private CourseStudentRepository courseStudentRepository;
//
//    @MockitoBean
//    private ReplyingKafkaTemplate<String, Object, UserExistsResponseEvent> replyingKafkaTemplate;
//
//    @Test
//    @DisplayName("Successfully create a course when instructor is valid")
//    void testCreateCourse() throws Exception {
//        // Arrange
//        CreateCourseRequest request = new CreateCourseRequest();
//        request.setInstructorID(123L);
//        request.setTitle("Test Course");
//        request.setDescription("Test Description");
//        request.setMaxStudents(20);
//
//        // Mock Kafka reply
//        UserExistsResponseEvent responseEvent = new UserExistsResponseEvent(0L,true,"");
//        ConsumerRecord<String, UserExistsResponseEvent> consumerRecord =
//                new ConsumerRecord<>("user-validation-response", 0, 0L, null, responseEvent);
//        RequestReplyFuture<String, Object, UserExistsResponseEvent> future = mock(RequestReplyFuture.class);
//
//        when(replyingKafkaTemplate.sendAndReceive(any(ProducerRecord.class))).thenReturn(future);
//        when(future.get()).thenReturn(consumerRecord);
//
//        // Mock repository
//        CourseEntity savedEntity = CourseEntity.builder()
//                .id(1L)
//                .title("Test Course")
//                .description("Test Description")
//                .instructorId(123L)
//                .maxStudents(20)
//                .modules(Collections.emptyList())
//                .build();
//
//        when(courseRepository.save(any(CourseEntity.class))).thenReturn(savedEntity);
//
//        // Act
//        ServiceResult<CourseResponse> result = courseService.createCourse(request);
//
//        // Assert
//        assertTrue(result.isSuccess());
//        assertEquals("Test Course", result.getData().getTitle());
//        assertEquals(HttpStatus.CREATED, result.getHttpStatus());
//    }
//
//
//    @Test
//    @DisplayName("Test getCourses(CourseFilterParams); given CourseEntity() Description is 'Description'; then return size is two")
//
//    void testGetCourses_givenCourseEntityDescriptionIsDescription_thenReturnSizeIsTwo() {
//        // Arrange
//        CourseEntity courseEntity = new CourseEntity();
//        courseEntity.setDescription("The characteristics of someone or something");
//        courseEntity.setId(1L);
//        courseEntity.setInstructorId(1L);
//        courseEntity.setMaxStudents(3);
//        courseEntity.setModules(new ArrayList<>());
//        courseEntity.setTitle("Dr");
//
//        CourseEntity courseEntity2 = new CourseEntity();
//        courseEntity2.setDescription("Description");
//        courseEntity2.setId(2L);
//        courseEntity2.setInstructorId(2L);
//        courseEntity2.setMaxStudents(1);
//        courseEntity2.setModules(new ArrayList<>());
//        courseEntity2.setTitle("Mr");
//
//        ArrayList<CourseEntity> content = new ArrayList<>();
//        content.add(courseEntity2);
//        content.add(courseEntity);
//        when(courseRepository.findCourseEntitiesByFilters(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Long>any(),
//                Mockito.<Integer>any(), Mockito.any())).thenReturn(new PageImpl<>(content));
//
//        CourseFilterParams filterParams = new CourseFilterParams();
//        filterParams.setCourseId(1L);
//        filterParams.setInstructorId(1L);
//        filterParams.setMaxStudents(3);
//        filterParams.setModuleId(1L);
//        filterParams.setPage(1);
//        filterParams.setSize(3);
//
//        // Act
//        List<CourseResponse> actualCourses = courseService.getCourses(filterParams);
//
//        // Assert
//        verify(courseRepository).findCourseEntitiesByFilters(eq(1L), eq(1L), eq(1L), eq(3), isA(Pageable.class));
//        assertEquals(2, actualCourses.size());
//        CourseResponse getResult = actualCourses.get(0);
//        assertEquals("Description", getResult.getDescription());
//        CourseResponse getResult2 = actualCourses.get(1);
//        assertEquals("Dr", getResult2.getTitle());
//        assertEquals("Mr", getResult.getTitle());
//        assertEquals("The characteristics of someone or something", getResult2.getDescription());
//        assertEquals(1, getResult.getMaxStudents());
//        assertEquals(1L, getResult2.getId().longValue());
//        assertEquals(1L, getResult2.getInstructorID().longValue());
//        assertEquals(2L, getResult.getId().longValue());
//        assertEquals(2L, getResult.getInstructorID().longValue());
//        assertEquals(3, getResult2.getMaxStudents());
//        assertTrue(getResult2.getModules().isEmpty());
//    }
//
//
//    @Test
//    @DisplayName("Test getCourses(CourseFilterParams); then calls getDescription()")
//
//    void testGetCourses_thenCallsGetDescription() {
//        // Arrange
//        CourseEntity courseEntity = mock(CourseEntity.class);
//        when(courseEntity.getMaxStudents()).thenReturn(3);
//        when(courseEntity.getId()).thenReturn(1L);
//        when(courseEntity.getInstructorId()).thenReturn(1L);
//        when(courseEntity.getDescription()).thenReturn("The characteristics of someone or something");
//        when(courseEntity.getTitle()).thenReturn("Dr");
//        when(courseEntity.getModules()).thenReturn(new ArrayList<>());
//        doNothing().when(courseEntity).setDescription(Mockito.any());
//        doNothing().when(courseEntity).setId(Mockito.<Long>any());
//        doNothing().when(courseEntity).setInstructorId(Mockito.<Long>any());
//        doNothing().when(courseEntity).setMaxStudents(anyInt());
//        doNothing().when(courseEntity).setModules(Mockito.any());
//        doNothing().when(courseEntity).setTitle(Mockito.any());
//        courseEntity.setDescription("The characteristics of someone or something");
//        courseEntity.setId(1L);
//        courseEntity.setInstructorId(1L);
//        courseEntity.setMaxStudents(3);
//        courseEntity.setModules(new ArrayList<>());
//        courseEntity.setTitle("Dr");
//
//        ArrayList<CourseEntity> content = new ArrayList<>();
//        content.add(courseEntity);
//        when(courseRepository.findCourseEntitiesByFilters(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Long>any(),
//                Mockito.<Integer>any(), Mockito.any())).thenReturn(new PageImpl<>(content));
//
//        CourseFilterParams filterParams = new CourseFilterParams();
//        filterParams.setCourseId(1L);
//        filterParams.setInstructorId(1L);
//        filterParams.setMaxStudents(3);
//        filterParams.setModuleId(1L);
//        filterParams.setPage(1);
//        filterParams.setSize(3);
//
//        // Act
//        List<CourseResponse> actualCourses = courseService.getCourses(filterParams);
//
//        // Assert
//        verify(courseEntity).getDescription();
//        verify(courseEntity).getId();
//        verify(courseEntity).getInstructorId();
//        verify(courseEntity).getMaxStudents();
//        verify(courseEntity).getModules();
//        verify(courseEntity).getTitle();
//        verify(courseEntity).setDescription(eq("The characteristics of someone or something"));
//        verify(courseEntity).setId(eq(1L));
//        verify(courseEntity).setInstructorId(eq(1L));
//        verify(courseEntity).setMaxStudents(eq(3));
//        verify(courseEntity).setModules(isA(List.class));
//        verify(courseEntity).setTitle(eq("Dr"));
//        verify(courseRepository).findCourseEntitiesByFilters(eq(1L), eq(1L), eq(1L), eq(3), isA(Pageable.class));
//        assertEquals(1, actualCourses.size());
//        CourseResponse getResult = actualCourses.getFirst();
//        assertEquals("Dr", getResult.getTitle());
//        assertEquals("The characteristics of someone or something", getResult.getDescription());
//        assertEquals(1L, getResult.getId().longValue());
//        assertEquals(1L, getResult.getInstructorID().longValue());
//        assertEquals(3, getResult.getMaxStudents());
//    }
//
//
//    @Test
//    @DisplayName("Test getCourses(CourseFilterParams); then return Empty")
//
//    void testGetCourses_thenReturnEmpty() {
//        // Arrange
//        when(courseRepository.findCourseEntitiesByFilters(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Long>any(),
//                Mockito.<Integer>any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
//
//        CourseFilterParams filterParams = new CourseFilterParams();
//        filterParams.setCourseId(1L);
//        filterParams.setInstructorId(1L);
//        filterParams.setMaxStudents(3);
//        filterParams.setModuleId(1L);
//        filterParams.setPage(1);
//        filterParams.setSize(3);
//
//        // Act
//        List<CourseResponse> actualCourses = courseService.getCourses(filterParams);
//
//        // Assert
//        verify(courseRepository).findCourseEntitiesByFilters(eq(1L), eq(1L), eq(1L), eq(3), isA(Pageable.class));
//        assertTrue(actualCourses.isEmpty());
//    }
//
//
//    @Test
//    @DisplayName("Test getCourses(CourseFilterParams); then return size is one")
//
//    void testGetCourses_thenReturnSizeIsOne() {
//        // Arrange
//        CourseEntity courseEntity = new CourseEntity();
//        courseEntity.setDescription("The characteristics of someone or something");
//        courseEntity.setId(1L);
//        courseEntity.setInstructorId(1L);
//        courseEntity.setMaxStudents(3);
//        courseEntity.setModules(new ArrayList<>());
//        courseEntity.setTitle("Dr");
//
//        ArrayList<CourseEntity> content = new ArrayList<>();
//        content.add(courseEntity);
//        when(courseRepository.findCourseEntitiesByFilters(Mockito.<Long>any(), Mockito.<Long>any(), Mockito.<Long>any(),
//                Mockito.<Integer>any(), Mockito.any())).thenReturn(new PageImpl<>(content));
//
//        CourseFilterParams filterParams = new CourseFilterParams();
//        filterParams.setCourseId(1L);
//        filterParams.setInstructorId(1L);
//        filterParams.setMaxStudents(3);
//        filterParams.setModuleId(1L);
//        filterParams.setPage(1);
//        filterParams.setSize(3);
//
//        // Act
//        List<CourseResponse> actualCourses = courseService.getCourses(filterParams);
//
//        // Assert
//        verify(courseRepository).findCourseEntitiesByFilters(eq(1L), eq(1L), eq(1L), eq(3), isA(Pageable.class));
//        assertEquals(1, actualCourses.size());
//        CourseResponse getResult = actualCourses.getFirst();
//        assertEquals("Dr", getResult.getTitle());
//        assertEquals("The characteristics of someone or something", getResult.getDescription());
//        assertEquals(1L, getResult.getId().longValue());
//        assertEquals(1L, getResult.getInstructorID().longValue());
//        assertEquals(3, getResult.getMaxStudents());
//    }
//
//
//    @Test
//    @DisplayName("Test assignStudentToCourse(AssignStudentToCourseRequest); then return MessageError is 'The course was not found'")
//
//    void testAssignStudentToCourse_thenReturnMessageErrorIsTheCourseWasNotFound() {
//        // Arrange
//        CourseRepository courseRepository = mock(CourseRepository.class);
//        Optional<CourseEntity> emptyResult = Optional.empty();
//        when(courseRepository.findCourseEntitiesById(Mockito.<Long>any())).thenReturn(emptyResult);
//        ProducerFactory<String, Object> producerFactory = mock(ProducerFactory.class);
//        when(producerFactory.transactionCapable()).thenReturn(true);
//        DefaultKafkaConsumerFactory<? super String, ? super UserExistsResponseEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(
//                new HashMap<>());
//        CourseService courseService = new CourseService(courseRepository, mock(CourseStudentRepository.class),
//                new ReplyingKafkaTemplate<>(producerFactory, new ConcurrentMessageListenerContainer<>(consumerFactory,
//                        new ContainerProperties(Pattern.compile(".*\\.txt")))));
//
//        // Act
//        ServiceResult<Boolean> actualAssignStudentToCourseResult = courseService
//                .assignStudentToCourse(new AssignStudentToCourseRequest());
//
//        // Assert
//        verify(courseRepository).findCourseEntitiesById(isNull());
//        verify(producerFactory).transactionCapable();
//        assertEquals("The course was not found", actualAssignStudentToCourseResult.getMessageError());
//        assertNull(actualAssignStudentToCourseResult.getData());
//        assertEquals(HttpStatus.NOT_FOUND, actualAssignStudentToCourseResult.getHttpStatus());
//        assertFalse(actualAssignStudentToCourseResult.isSuccess());
//    }
//}
