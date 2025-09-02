// Inspired by react-hot-toast library
import { useState, createContext, useContext } from "react"

const TOAST_LIMIT = 5
const TOAST_REMOVE_DELAY = 5000

const actionTypes = {
  ADD_TOAST: "ADD_TOAST",
  UPDATE_TOAST: "UPDATE_TOAST",
  DISMISS_TOAST: "DISMISS_TOAST",
  REMOVE_TOAST: "REMOVE_TOAST"
}

let count = 0

function genId() {
  count = (count + 1) % Number.MAX_SAFE_INTEGER
  return count.toString()
}

const toastTimeouts = new Map()

const addToRemoveQueue = (toastId) => {
  if (toastTimeouts.has(toastId)) {
    return
  }

  const timeout = setTimeout(() => {
    toastTimeouts.delete(toastId)
    dispatch({
      type: actionTypes.REMOVE_TOAST,
      toastId: toastId,
    })
  }, TOAST_REMOVE_DELAY)

  toastTimeouts.set(toastId, timeout)
}

export const reducer = (state, action) => {
  switch (action.type) {
    case actionTypes.ADD_TOAST:
      return {
        ...state,
        toasts: [action.toast, ...state.toasts].slice(0, TOAST_LIMIT),
      }

    case actionTypes.UPDATE_TOAST:
      return {
        ...state,
        toasts: state.toasts.map((t) =>
          t.id === action.toast.id ? { ...t, ...action.toast } : t
        ),
      }

    case actionTypes.DISMISS_TOAST: {
      const { toastId } = action

      // ! Side effects ! - This could be extracted into a dismissToast() action,
      // but I'll keep it here for simplicity
      if (toastId) {
        addToRemoveQueue(toastId)
      } else {
        state.toasts.forEach((toast) => {
          addToRemoveQueue(toast.id)
        })
      }

      return {
        ...state,
        toasts: state.toasts.map((t) =>
          t.id === toastId || toastId === undefined
            ? {
                ...t,
                open: false,
              }
            : t
        ),
      }
    }
    case actionTypes.REMOVE_TOAST:
      if (action.toastId === undefined) {
        return {
          ...state,
          toasts: [],
        }
      }
      return {
        ...state,
        toasts: state.toasts.filter((t) => t.id !== action.toastId),
      }
  }
}

const initialState = {
  toasts: [],
}

const ToastContext = createContext({})

function useToastContext() {
  const context = useContext(ToastContext)

  if (context === undefined) {
    throw new Error("useToast must be used within a ToastProvider")
  }

  return context
}

export const ToastProvider = ({ children }) => {
  const [state, dispatch] = useState(initialState)

  return (
    <ToastContext.Provider value={{ state, dispatch }}>
      {children}
    </ToastContext.Provider>
  )
}

export const useToast = () => {
  const { dispatch } = useToastContext()

  return {
    toast: (props) => {
      const id = props.id || genId()

      dispatch({
        type: actionTypes.ADD_TOAST,
        toast: {
          id,
          open: true,
          ...props,
        },
      })

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
      }
    },
    dismiss: (toastId) => dispatch({ type: actionTypes.DISMISS_TOAST, toastId }),
  }
}
