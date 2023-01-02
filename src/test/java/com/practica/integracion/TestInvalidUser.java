package com.practica.integracion;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {
	public AuthDAO mockAuthDao;
	public GenericDAO mockGenericDao;
	@BeforeEach
	public void setUp(){
		mockAuthDao = Mockito.mock(AuthDAO.class);
		mockGenericDao = Mockito.mock(GenericDAO.class);
	}
	@Test
	public void testStartRemoteSystemWithInvalidUserAndSystem() throws Exception{
		User invalidUser = new User("1","Perico", "De los palotes", "Madrid", new ArrayList<Object>(Arrays.asList(1,2)));
		when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(null);
		
		String invalidId = "12345";
		when(mockGenericDao.getSomeData(null, "where id=" + invalidId)).thenThrow(OperationNotSupportedException.class);
		
		SystemManager system = new SystemManager(mockAuthDao,mockGenericDao);
		
		Assertions.assertThrows(SystemManagerException.class, ()->{
			system.startRemoteSystem(invalidUser.getId(), invalidId);
		}, "usuario invalido debería lanzar una excepción");
	}
	@Test
	public void testStopRemoteSystemWithInvalidUserAndSystem() throws Exception{
		User invalidUser = new User("1","Perico", "De los palotes", "Madrid", new ArrayList<Object>(Arrays.asList(1,2)));
		when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(null);
		
		String invalidId = "12345";
		when(mockGenericDao.getSomeData(null, "where id=" + invalidId)).thenThrow(OperationNotSupportedException.class);
		
		SystemManager system = new SystemManager(mockAuthDao,mockGenericDao);
		Assertions.assertThrows(SystemManagerException.class, ()->{
			system.stopRemoteSystem(invalidUser.getId(), invalidId);
		}, "usuario inválido debería lanzar una excepción");
	}
	@Test
	public void testAddRemoteSystemWithInvalidUserAndSystem() throws Exception{
		User invalidUser = new User("1","Perico", "De los palotes", "Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(null);

		ArrayList<Object> lista = new ArrayList<>();
		when(mockGenericDao.updateSomeData(null, lista)).thenThrow(OperationNotSupportedException.class);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);

		Assertions.assertThrows(SystemManagerException.class, () -> {
			manager.addRemoteSystem(invalidUser.getId(), lista);
		}, "usuario invalido, da excepción");

		ordered.verify(mockAuthDao).getAuthData(invalidUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(null, lista);
	}
}
