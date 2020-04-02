(ns essen-editor.events.function
  (:require
   [essen-editor.req :refer [fs]]
   [essen-editor.parser :as parser]
   [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 ::replace
 (fn [{:keys [db]} [_]]
   (let [{:file/keys [name content function]} db
         contents (.toString (.readFileSync fs name))
         {:instaparse.gll/keys [start-index end-index]}
         (get (parser/file->functions contents) function)
         new-contents
         (str (subs contents 0 start-index ) content (subs contents end-index))]
     (.writeFile fs name new-contents
                 (fn [error]
                   (when error
                     (println "ERROR: Failed writing to file ?" name error)))))))
