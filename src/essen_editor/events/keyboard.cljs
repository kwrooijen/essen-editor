(ns essen-editor.events.keyboard
  (:require
   [essen-editor.events.function :as events.function]
   [re-frame.core :as re-frame]))

(re-frame/reg-event-db
 ::set-cmd
 (fn [db [_ v]]
   (assoc db :cmd/down v)))

(re-frame/reg-event-fx
 ::submit
 (fn [{:keys [db]} [_ k]]
   (condp = (.-keyCode k)
     91 (re-frame/dispatch [::set-cmd true])
     13 (when (:cmd/down db)
          (re-frame/dispatch [::events.function/replace (:file/content db)]))
     nil)
   {}))

(re-frame/reg-event-fx
 ::add-event-listener
 (fn [_ _]
   (.addEventListener
    (js/document.getElementById "function-code")
    "keydown"
    (fn [k]
      (re-frame/dispatch [::submit k])))

   (.addEventListener
    (js/document.getElementById "function-code")
    "keyup"
    (fn [k]
      (condp = (.-keyCode k)
        91 (re-frame/dispatch [::set-cmd false])
        {})))))
