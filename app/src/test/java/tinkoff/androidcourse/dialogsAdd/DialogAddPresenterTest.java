package tinkoff.androidcourse.dialogsAdd;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
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

public class DialogAddPresenterTest {

    // Creates a spy of the real object. The spy calls <b>real</b> methods unless they are stubbed.
    private final DialogAddPresenter dialogAddPresenter = spy(new DialogAddPresenter());
    // Mock objects imitate the dependencies of the object we are testing.
    private final DialogAddView dialogAddView = mock(DialogAddView.class);

    @Before
    public void setUp() throws Exception{
        //call of dialogAddPresenter.getView() returns dialogAddView
        doReturn(dialogAddView).when(dialogAddPresenter).getView();
    }

    /*
    * check that after attachView() if "result" == null never call onDecision()
    * */
    @Test
    public void when_attachView_n_result_null_DoNothing() throws Exception{
        dialogAddPresenter.attachView(dialogAddView);
        //Verifies certain behavior happened at least once / exact number of times / never.
        //myFunc(anyBoolean()) means call myFunc with any Boolean param
        verify(dialogAddPresenter, never()).onDecision(anyBoolean());
    }

    /*
    * check if "result" not null (false here), then call onAuthResult() & .authResult not null now
    * */
    @Test
    public void when_attachView_n_result_notNull_call_onAuthResult_n_clearResult() throws Exception{
        dialogAddPresenter.error_code = -1;
        Boolean result = (dialogAddPresenter.result = false);
        dialogAddPresenter.attachView(dialogAddView);
        //Verifies certain behavior <b>happened once</b>.
        verify(dialogAddPresenter).onDecision(result);
        assertNull(dialogAddPresenter.result);
    }

    /*
    * check if setAuthResult() calls onAuthResult() if isViewAttached()
    * */
    @Test
    public void viewAttached_when_setAuthResult_then_onAuthResult_with_result() throws Exception{
        doReturn(true).when(dialogAddPresenter).isViewAttached();
        Boolean result = true;
        dialogAddPresenter.setAuthResult(result);
        verify(dialogAddPresenter).onDecision(result);
    }

    /*
    * check if setAuthResult() set .authResult to "result" value (will not be null)
    * */
    @Test
    public void notViewAttached_when_setAuthResult_then_setAuthResult() throws Exception{
        doReturn(true).when(dialogAddPresenter).isViewAttached();
        Boolean result = true;
        dialogAddPresenter.setAuthResult(result);
        assertNull(dialogAddPresenter.result);
    }

    /*
    * check if we go to Navigation screen after successful authorization
    * */
    @Test
    public void resultTrue_when_onAuthResult_then_redirectToNavigation() throws Exception{
        dialogAddPresenter.onDecision(true);
        verify(dialogAddView).redirectToNavigationWithExtra(null, null);
    }

    /*
    * check if we show FAIL alert after unsuccessful auth
    * */
    @Test
    public void resultFalse_when_onAuthResult_then_showFailedAuth() throws Exception{
        dialogAddPresenter.error_code = dialogAddPresenter.ERROR_NO_NAME; //for example
        dialogAddPresenter.onDecision(false);
        verify(dialogAddView).showFailedPopup(dialogAddPresenter.error_code);
    }

    /*
    * check error codes on Title & Descr input
    * */
    @Test
    public void onCheckInput_wrongStringTitleEmpty() throws Exception{
        dialogAddPresenter.title = "";
        dialogAddPresenter.descr = "descrTest";
        dialogAddPresenter.onCheckInput(dialogAddPresenter.title, dialogAddPresenter.descr);
        assertTrue(dialogAddPresenter.error_code.equals(dialogAddPresenter.ERROR_NO_NAME));
    }

    @Test
    public void onCheckInput_wrongStringTitleShort() throws Exception{
        dialogAddPresenter.title = "12";    //2 symbols < 3
        dialogAddPresenter.descr = "descrTest";
        dialogAddPresenter.onCheckInput(dialogAddPresenter.title, dialogAddPresenter.descr);
        assertTrue(dialogAddPresenter.error_code.equals(dialogAddPresenter.ERROR_TOO_SHORT_NAME));
    }

    @Test
    public void onCheckInput_wrongStringDescrEmpty() throws Exception{
        dialogAddPresenter.title = "titleTest";
        dialogAddPresenter.descr = "";
        dialogAddPresenter.onCheckInput(dialogAddPresenter.title, dialogAddPresenter.descr);
        assertTrue(dialogAddPresenter.error_code.equals(dialogAddPresenter.ERROR_NO_DESCR));
    }

    @Test
    public void onCheckInput_wrongStringDescrShort() throws Exception{
        dialogAddPresenter.title = "titleTest";
        dialogAddPresenter.descr = "12"; //2 symbols < 3
        dialogAddPresenter.onCheckInput(dialogAddPresenter.title, dialogAddPresenter.descr);
        assertTrue(dialogAddPresenter.error_code.equals(dialogAddPresenter.ERROR_TOO_SHORT_DESCR));
    }

}
