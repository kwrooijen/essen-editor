(ns essen-editor.views
  (:require
   [re-frame.core :as re-frame]
   [essen-editor.events :as event]
   [essen-editor.subs :as subs]))


(defn main-panel []
  (re-frame/dispatch [::event/read-file "/Users/kwrooijen/example.clj"])
  [:div "Hello"])
