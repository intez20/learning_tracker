# Toast Notification System in Learning Tracker

This document describes the implementation of the toast notification system in the Learning Tracker application.

## Overview

The toast notification system provides feedback to users after actions like creating, updating, or deleting learning items. It uses a React context to manage the state of toast messages and renders them in a fixed position on the screen.

## Components

### 1. ToastProvider (`use-toast.jsx`)

The `ToastProvider` component creates a React context that manages the state of all toast notifications. It provides:

- Toast state management with add, update, dismiss, and remove actions
- Automatic timeout for dismissing toasts
- Context provider for the entire application

```jsx
export const ToastProvider = ({ children }) => {
  const [state, dispatchFn] = useState(initialState);
  
  // Assign dispatch function
  dispatch = dispatchFn;

  return (
    <ToastContext.Provider value={{ state, dispatch: dispatchFn }}>
      {children}
    </ToastContext.Provider>
  );
};
```

### 2. useToast Hook (`use-toast.jsx`)

The `useToast` hook provides a simple API for components to create, update, and dismiss toast notifications:

```jsx
export const useToast = () => {
  const { dispatch } = useToastContext();

  return {
    toast: (props) => {
      const id = props.id || genId();

      dispatch({
        type: actionTypes.ADD_TOAST,
        toast: {
          id,
          open: true,
          ...props,
        },
      });

      return {
        id,
        dismiss: () => dispatch({ type: actionTypes.DISMISS_TOAST, toastId: id }),
        update: (props) => dispatch({
          type: actionTypes.UPDATE_TOAST,
          toast: {
            id,
            ...props,
          },
        }),
      };
    },
    dismiss: (toastId) => dispatch({ type: actionTypes.DISMISS_TOAST, toastId }),
  };
};
```

### 3. Toaster Component (`toaster-new.jsx`)

The `Toaster` component renders all active toast notifications. It:

- Displays toasts in a fixed position on the screen
- Handles animations for appearing and disappearing
- Renders the title and description of each toast

```jsx
export function Toaster() {
  const { state } = useToast();

  return (
    <div className="fixed top-0 right-0 z-50 flex flex-col gap-2 w-full max-w-sm p-4 sm:top-auto sm:bottom-0">
      {state.toasts && state.toasts.map((toast) => (
        <div
          key={toast.id}
          className={`bg-white dark:bg-gray-800 rounded-md shadow-lg p-4 transition-all duration-300 ease-in-out ${
            toast.open ? "opacity-100 translate-y-0" : "opacity-0 translate-y-2"
          }`}
          style={{ display: toast.open ? "flex" : "none" }}
        >
          <div className="flex flex-col">
            {toast.title && (
              <div className="font-semibold text-sm">{toast.title}</div>
            )}
            {toast.description && (
              <div className="text-sm text-gray-500 dark:text-gray-400 mt-1">
                {toast.description}
              </div>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}
```

## Integration in the Application

1. **Setup in `main.jsx`**
   The `ToastProvider` wraps the entire application:

   ```jsx
   <ToastProvider>
     <App />
   </ToastProvider>
   ```

2. **Adding the Toaster in `App.jsx`**
   The `Toaster` component is rendered at the root level:

   ```jsx
   function App() {
     return (
       <Router>
         <Layout>
           <Routes>
             {/* Routes */}
           </Routes>
         </Layout>
         <Toaster />
       </Router>
     )
   }
   ```

3. **Using Toasts in Components**
   The `useToast` hook is used in components to show notifications:

   ```jsx
   const { toast } = useToast();
   
   // Success notification
   toast({
     title: "Success!",
     description: "Learning item created successfully."
   });
   
   // Error notification
   toast({
     title: "Error",
     description: "Failed to create learning item.",
     variant: "destructive"
   });
   ```

## Customization

The toast notifications can be customized by:

1. Modifying the styling in `toaster-new.jsx`
2. Adjusting the timeout in `use-toast.jsx` (`TOAST_REMOVE_DELAY`)
3. Changing the maximum number of toasts shown at once (`TOAST_LIMIT`)

## Future Enhancements

Potential improvements:

1. Add different toast variants (success, error, warning, info)
2. Add close button to manually dismiss toasts
3. Customize positioning (top, bottom, left, right)
4. Add custom actions to toasts
