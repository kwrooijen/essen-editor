(ns essen-editor.events
  (:require
   [essen-editor.events.keyboard]
   [essen-editor.parser :as parser]
   [re-frame.core :as re-frame]
   [essen-editor.db :as db]
   [essen-editor.req :refer [fs]]))

(defn get-index [contents function]
  (let [{:instaparse.gll/keys [start-index end-index]}
        (get (parser/file->functions contents) function)]
    [start-index end-index]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::save-file-contents
 (fn [db [_ contents file-name]]
   (let [functions (keys (parser/file->functions contents))
         [start-index end-index] (get-index contents (first functions))]
     (assoc db
            :file/name file-name
            :file/functions functions
            :file/function (first functions)
            :file/ns (namespace (first (keys (parser/file->functions contents))))
            :file/content (subs contents start-index end-index)))))

(re-frame/reg-event-db
 ::update-content
 (fn [db [_ content]]
   (assoc db :file/content content)))

(re-frame/reg-event-fx
 ::read-file
 (fn [_ [_ file-name]]
   (.readFile fs file-name
              (fn [error file]
                (when error
                  (println "ERROR READING FILE: " error))
                (re-frame/dispatch [::save-file-contents
                                    (.toString file)
                                    file-name])))))

(re-frame/reg-event-db
 ::set-function
 (fn [db [_ function]]
   (let [content (.toString (.readFileSync fs (:file/name db)))
         function (keyword (:file/ns db) function)
         [start-index end-index] (get-index content function)]
     (assoc db
            :file/function function
            :file/content (subs content start-index end-index)))))
