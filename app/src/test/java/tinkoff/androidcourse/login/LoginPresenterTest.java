package tinkoff.androidcourse.login;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created on 22.05.2017
 *
 * @author Puzino Yury
 */

public class LoginPresenterTest {

    // Creates a spy of the real object. The spy calls <b>real</b> methods unless they are stubbed.
    private final LoginPresenter loginPresenter = spy(new LoginPresenter());
    // Mock objects imitate the dependencies of the object we are testing.
    private final LoginView loginView = mock(LoginView.class);

    @Before
    public void setUp() throws Exception{
        //call of loginPresenter.getView() returns loginView
        doReturn(loginView).when(loginPresenter).getView();
    }

    /*
    * check that after attachView() if "result" == null never call onAuthResult()
    * */
    @Test
    public void when_attachView_n_result_null_DoNothing() throws Exception{
        loginPresenter.attachView(loginView);
        //Verifies certain behavior happened at least once / exact number of times / never.
        //myFunc(anyBoolean()) means call myFunc with any Boolean param
        verify(loginPresenter, never()).onAuthResult(anyBoolean());
    }

    /*
    * check if "result" not null (false here), then call onAuthResult() & .authResult not null now
    * */
    @Test
    public void when_attachView_n_result_notNull_call_onAuthResult_n_clearResult() throws Exception{
        Boolean result = (loginPresenter.authResult = false);
        loginPresenter.attachView(loginView);
        //Verifies certain behavior <b>happened once</b>.
        verify(loginPresenter).onAuthResult(result);
        assertNull(loginPresenter.authResult);
    }

    /*
    * check if setAuthResult() calls onAuthResult() if isViewAttached()
    * */
    @Test
    public void viewAttached_when_setAuthResult_then_onAuthResult_with_result() throws Exception{
        doReturn(true).when(loginPresenter).isViewAttached();
        Boolean result = true;
        loginPresenter.setAuthResult(result);
        verify(loginPresenter).onAuthResult(result);
    }

    /*
    * check if setAuthResult() set .authResult to "result" value (will not be null)
    * */
    @Test
    public void notViewAttached_when_setAuthResult_then_setAuthResult() throws Exception{
        doReturn(true).when(loginPresenter).isViewAttached();
        Boolean result = true;
        loginPresenter.setAuthResult(result);
        assertNull(loginPresenter.authResult);
    }

    /*
    * check if we go to Navigation screen after successful authorization
    * */
    @Test
    public void resultTrue_when_onAuthResult_then_redirectToNavigation() throws Exception{
        loginPresenter.onAuthResult(true);
        verify(loginView).redirectToNavigation();
    }

    /*
    * check if we show FAIL alert after unsuccessful auth
    * */
    @Test
    public void resultFalse_when_onAuthResult_then_showFailedAuth() throws Exception{
        loginPresenter.onAuthResult(false);
        verify(loginView).showFailedAuth();
    }

}
