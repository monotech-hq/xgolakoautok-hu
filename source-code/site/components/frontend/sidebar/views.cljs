
(ns site.components.frontend.sidebar.views
    (:require [elements.api                             :as elements]
              [random.api                               :as random]
              [re-frame.api                             :as r]
              [site.components.frontend.sidebar.helpers :as sidebar.helpers]
              [x.components.api                         :as x.components]
              [x.environment.api                        :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-close-button
  ; @param (keyword) component-id
  ; @param (map) component-props
  [_ _]
  [:div {:style {:position "absolute" :right "12px" :top "12px"}}
        [elements/icon-button ::sidebar-close-button
                              {:icon :close :on-click [:components.sidebar/hide-sidebar!]}]])

(defn- sidebar
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :style (map)(opt)}
  [component-id {:keys [class content style] :as component-props}]
  (let [sidebar-visible? @(r/subscribe [:components.sidebar/sidebar-visible?])]
       [:div {:id :mt-sidebar :class class :style style
              :data-visible (boolean sidebar-visible?)}
             [:div {:id :mt-sidebar--cover :on-click #(r/dispatch [:components.sidebar/hide-sidebar!])}]
             [:div {:id :mt-sidebar--content}
                   [x.components/content component-id content]
                   [sidebar-close-button component-id component-props]]]))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [sidebar {...}]
  ;
  ; @usage
  ;  [sidebar :my-sidebar {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [sidebar component-id component-props]))
