package tinkoff.androidcourse.login;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * @author Sergey Boishtyan
 */
public class LoginPresenterTest {

    private final LoginPresenter loginPresenterSpy = spy(new LoginPresenter());
    private final LoginView viewMock = mock(LoginView.class);

    @Before
    public void setUp() throws Exception {
        doReturn(viewMock).when(loginPresenterSpy).getView();
    }

    @Test
    public void when_attachView_and_result_null_do_nothing() throws Exception {
        loginPresenterSpy.attachView(viewMock);
        verify(loginPresenterSpy, never()).onAuthorizationResult(anyBoolean());
    }

    @Test
    public void when_attachView_and_result_not_null_call_onAuthorizationResult_and_clear_result() throws Exception {
        Boolean result = loginPresenterSpy.authorizationResult = false;
        loginPresenterSpy.attachView(viewMock);
        verify(loginPresenterSpy).onAuthorizationResult(result);
        assertNull(loginPresenterSpy.authorizationResult);
    }

    @Test
    public void given_viewAttached_when_setAuthorizationResult_then_onAuthorizationResult_with_result() throws Exception {
        Mockito.doReturn(true).when(loginPresenterSpy).isViewAttached();
        Boolean authorizationResult = true;
        loginPresenterSpy.setAuthorizationResult(authorizationResult);
        verify(loginPresenterSpy).onAuthorizationResult(authorizationResult);
    }

    @Test
    public void given_not_viewAttached_when_setAuthorizationResult_then_set_authorizationResult() throws Exception {
        Mockito.doReturn(true).when(loginPresenterSpy).isViewAttached();
        Boolean authorizationResult = true;
        loginPresenterSpy.setAuthorizationResult(authorizationResult);
        assertNull(loginPresenterSpy.authorizationResult);
    }

    @Test
    public void given_result_true_when_onAuthorizationResult_then_goToNavigationScreen() throws Exception {
        loginPresenterSpy.onAuthorizationResult(true);
        verify(viewMock).goToNavigationScreen();
    }

    @Test
    public void given_result_false_when_onAuthorizationResult_then_showUnSuccessAuthorization() throws Exception {
        loginPresenterSpy.onAuthorizationResult(false);
        verify(viewMock).showUnSuccessAuthorization();
    }
}
