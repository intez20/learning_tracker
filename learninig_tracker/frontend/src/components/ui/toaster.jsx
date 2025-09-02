import * as React from "react"
import { useToast } from "./use-toast.jsx"

export function Toaster() {
  const { state } = useToast()

  return (
    <div className="fixed top-0 right-0 z-50 flex flex-col gap-2 w-full max-w-sm p-4 sm:top-auto sm:bottom-0">
      {state.toasts.map((toast) => (
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
  )
}
}
}
