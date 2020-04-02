(ns essen-editor.views
  (:require
   [essen-editor.events.keyboard :as event.keyboard]
   [re-frame.core :as re-frame]
   [essen-editor.events :as event]
   [essen-editor.subs :as subs]))

(def file "/Users/kwrooijen/example.clj")

(defn function-selector
  ""
  []
  [:select
   {:on-change #(re-frame/dispatch [::event/set-function (.-value (.-target %))])}
   (doall
    (for [function @(re-frame/subscribe [::subs/functions])]
      ^{:key function}
      [:option {:value function}
       function]))])

(defn main-panel []
  (re-frame/dispatch [::event/read-file file])
  (re-frame/dispatch [::event.keyboard/add-event-listener])
  (fn []
    (let [content (re-frame/subscribe [::subs/content])]
      [:div
       [:div.flex.flex-column.justify-center.items-center.h-full
        [:div.title
         "Editting: " file]
        [:div.selector
         [:div.label
          [:label "Select function:"]]
         [function-selector]]
        [:textarea#function-code
         {:value @content
          :on-change #(re-frame/dispatch [::event/update-content (.-value (.-target %))])}]]])))
