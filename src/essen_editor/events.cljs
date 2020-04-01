(ns essen-editor.events
  (:require
   [re-frame.core :as re-frame]
   [essen-editor.db :as db]
   [essen-editor.fs :refer [fs]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::save-file-contents
 (fn [db [_ content]]
   (assoc db :file/content content)))


(re-frame/reg-event-fx
 ::read-file
 (fn [{:keys [db]} [_ file-name]]
   (.readFile fs file-name
              (fn [error file]
                (when error
                  (println "ERROR READING FILE: " error))
                (re-frame/dispatch [::save-file-contents (.toString file)])))))
