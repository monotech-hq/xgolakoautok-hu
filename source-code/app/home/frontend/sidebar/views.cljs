
(ns app.home.frontend.sidebar.views
    (:require [app.home.frontend.handler.config :as handler.config]
              [elements.api                     :as elements]
              [layouts.sidebar-a.api            :as sidebar-a]
              [vector.api                :as vector]
              [re-frame.api                     :as r]
              [x.components.api                 :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-group-item-icon
  ; @param (map) group-item
  ;  {:icon (keyword)
  ;   :icon-color (string)
  ;   :icon-family (keyword)}
  [{:keys [icon icon-color icon-family]}]
  [elements/icon {:color       icon-color
                  :icon        icon
                  :icon-family icon-family
                  :size        :s
                  :indent      {:horizontal :xs :left :s :right :xxs}}])

(defn- label-group-item-label
  ; @param (map) group-item
  ;  {:label (metamorphic-content)}
  [{:keys [label]}]
  [elements/label {:color       :invert
                   :content     label
                   :indent      {:right :xl}
                   :font-size   :xs
                   :line-height :block}])

(defn- label-group-item
  ; @param (map) group-item
  ;  {:disabled? (boolean)(opt)}
  ;   :icon (keyword)
  ;   :icon-color (string)
  ;   :icon-family (keyword)
  ;   :on-click (metamorphic-event)}
  [{:keys [disabled? icon icon-color icon-family label on-click] :as group-item}]
  [elements/toggle {:content     [:div {:style {:display :flex}}
                                       [label-group-item-icon  group-item]
                                       [label-group-item-label group-item]]
                    :disabled?   disabled?
                    :on-click    on-click
                    :hover-color :invert}])

(defn- label-group
  ; @param (metamorphic-content) label
  ; @param (?) label-group
  [label label-group]
  (letfn [(f [item-list group-item]
             (conj item-list [label-group-item group-item]))]
         (reduce f [:<>] label-group)))

(defn- weight-group
  ; @param (integer) vertical-weight
  ; @param (?) weight-group
  [vertical-weight weight-group]
  ; XXX#0091 (app.home.frontend.screen.views)
  (let [label-groups (group-by #(-> % :label x.components/content) weight-group)]
       (letfn [(f [group-list label]
                  (conj group-list [label-group label (get label-groups label)]))]
              (reduce f [:<>] (-> label-groups keys sort)))))

(defn- menu-group-label
  ; @param (metamorphic-content) group-name
  [group-name]
  [elements/label {:color       :invert
                   :content     group-name
                   :font-size   :xs
                   :indent      {:left :s :top :xs :right :l}
                   :line-height :block
                   :style       {:opacity ".6"}}])

(defn- menu-group
  ; @param (map) group-props
  ;  {:color (string)
  ;   :name (metamorphic-content)}
  [{:keys [name] :as group-props}]
  ; XXX#0091 (app.home.frontend.screen.views)
  (let [group-items @(r/subscribe [:home.sidebar/get-menu-group-items name])]
       (if (vector/nonempty? group-items)
           [:<> [menu-group-label name]
                (let [weight-groups (group-by :vertical-weight group-items)]
                     (letfn [(f [group-list vertical-weight]
                                (conj group-list [weight-group vertical-weight (get weight-groups vertical-weight)]))]
                            (reduce f [:div {:style {:padding-bottom "12px"}}] (-> weight-groups keys sort))))])))

(defn- menu-groups
  []
  ; XXX#0091 (app.home.frontend.screen.views)
  (letfn [(f [group-list group-props]
             (conj group-list [menu-group group-props]))]
         (reduce f [:<>] handler.config/GROUP-ORDER)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- app-home-button
  []
  [label-group-item {:icon :apps :label :app-home :icon-family :material-icons-outlined
                     :icon-color "#98829e" :on-click [:x.router/go-to! "/@app-home"]}])

(defn- label
  []
  (let [app-title @(r/subscribe [:x.core/get-app-config-item :app-title])]
       [elements/label ::label
                       {:color       "#bfbfbf"
                        :content     (string.api/uppercase app-title)
                        :font-size   :xs
                        :font-weight :extra-bold
                        :line-height :block
                        :indent      {:horizontal :xs :vertical :s}}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  ; @param (keyword) sidebar-id
  [_]
  (if @(r/subscribe [:x.environment/viewport-min? 1024])
       [:div {:style {:display "flex" :flex-direction "column" :height "100%"}}
             [:div {:style {:background-color "#00000021"}}
                   [label]]
             ;[elements/horizontal-line {:color "#88998e"}]
             [elements/horizontal-separator {:size :xs}]
             [:div {:data-scrollable-y true :style {:flex-grow "1"}}
                   [app-home-button]
                   [menu-groups]]]))

(defn view
  ; @param (keyword) sidebar-id
  [sidebar-id]

  ; TEMP
  ; BUG
  (let [js-build @(r/subscribe [:x.router/get-current-js-build])]
       (if (= js-build :app)

           [sidebar-a/layout sidebar-id
                             {:content #'view-structure}])))
