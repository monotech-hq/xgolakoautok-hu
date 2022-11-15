
(ns app.home.frontend.screen.views
    (:require [app.common.frontend.api          :as common]
              [app.home.frontend.handler.config :as handler.config]
              [elements.api                     :as elements]
              [layouts.surface-a.api            :as surface-a]
              [mid-fruits.css                   :as css]
              [mid-fruits.vector                :as vector]
              [re-frame.api                     :as r]
              [x.app-components.api             :as x.components]

              ; TEMP
              [mid-fruits.string :as string]))
              ; [mid-fruits.syntax :as syntax]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-profile-picture
  []
  (let [user-profile-picture @(r/subscribe [:user/get-user-profile-picture])]
       [:div.x-user-profile-picture {:style {:backgroundImage     (css/url user-profile-picture)
                                             :background-color    (css/var "background-color-highlight")
                                             :border-radius       "50%";
                                             :background-position "center"
                                             :background-repeat   "no-repeat"
                                             :background-size     "90%"
                                             :overflow            "hidden"
                                             :height              "60px"
                                             :width               "60px"}}]))

(defn- user-name-label
  []
  (let [user-name @(r/subscribe [:user/get-user-name])]
       [elements/label ::user-name-label
                       {:content     user-name
                        :font-size   :s
                        :font-weight :extra-bold
                        :indent      {:left :xs}
                        :style       {:line-height "18px"}}]))

(defn- user-email-address-label
  []
  (let [user-email-address (r/subscribe [:user/get-user-email-address])]
       [elements/label ::user-email-address-label
                       {:color     :muted
                        :content   user-email-address
                        :font-size :xs
                        :indent    {:left :xs}
                        :style     {:line-height "18px"}}]))

(defn- user-card
  []
  [:div {:style {:display :flex}}
        [:div {}
              [user-profile-picture]]
        [:div {:style {:display :flex :flex-direction :column :justify-content :center}}
              [user-name-label]
              [user-email-address-label]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-group-item-content
  ; @param (map) group-item
  ;  {}
  [{:keys [icon icon-color icon-family label]}]
  [:div {:style {:display :flex}}
        [elements/icon {:color       icon-color
                        :icon        icon
                        :icon-family icon-family
                        :indent      {:horizontal :xs}
                        :size        :l}]
        [elements/label {:content label
                         :indent  {}}]])

(defn- label-group-item
  ; @param (map) group-item
  ;  {}
  [{:keys [disabled? on-click] :as group-item}]
  [elements/card {:border-radius    :m
                  :content          [label-group-item-content group-item]
                  :disabled?        disabled?
                  :on-click         on-click
                  :horizontal-align :left
                  :hover-color      :highlight
                  ;:min-width        :s
                  :indent           {:vertical :xs}}])

(defn- label-group
  ; @param (metamorphic-content) label
  ; @param (?) label-group
  [label label-group]
  (letfn [(f [item-list group-item]
             (conj item-list [label-group-item group-item]))]
         (reduce f [:<>] label-group)))

(defn- weight-group
  ; @param (integer) horizontal-weight
  ; @param (?) weight-group
  [horizontal-weight weight-group]
  ; XXX#0091
  ; Az azonos menu-group csoportban és azon belül is azonos weight-group
  ; csoportban felsorolt menü elemek a label tulajdonságuk szerinti kisebb
  ; csoportokban vannak felsorolva.
  (let [label-groups (group-by #(-> % :label x.components/content) weight-group)]
       (letfn [(f [group-list label]
                  (conj group-list [label-group label (get label-groups label)]))]
              (reduce f [:<>] (-> label-groups keys sort)))))

(defn- menu-group
  ; @param (keyword) group-name
  [group-name]
  ; XXX#0091
  ; Az azonos menu-group csoportban felsorolt menü elemek a horizontal-weight
  ; tulajdonságuk szerinti kisebb csoportokban vannak felsorolva.
  (let [group-items @(r/subscribe [:home.screen/get-menu-group-items group-name])]
       (if (vector/nonempty? group-items)
           [common/surface-box {:content [:div {:style {:display "grid" :grid-row-gap "12px" :padding "12px 0"
                                                        :grid-template-columns "repeat(auto-fill, minmax(240px, 1fr))"}}
                                               (let [weight-groups (group-by :horizontal-weight group-items)]
                                                    (letfn [(f [group-list horizontal-weight]
                                                               (conj group-list [weight-group horizontal-weight (get weight-groups horizontal-weight)]))]
                                                           (reduce f [:<>] (-> weight-groups keys sort))))]
                                :indent {:top :m}
                                :label  group-name}])))

(defn- menu-groups
  []
  ; XXX#0091
  ; A menü elemek elsődlegesen a group tulajdonságuk szerint csoportosítva
  ; vannak felsorolva a vertical-group csoportokban.
  (letfn [(f [group-list group-name]
             (conj group-list [menu-group group-name]))]
         (reduce f [:<>] handler.config/GROUP-ORDER)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs
  []
  [common/surface-breadcrumbs :home.screen/view
                              {:crumbs [{:label :app-home}]}])

(defn- label
  []
  (let [app-title @(r/subscribe [:x.core/get-app-config-item :app-title])]
       [common/surface-label :home.screen/view
                             {:label app-title}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  ; @param (keyword) surface-id
  [_]
  (if-let [screen-loaded? @(r/subscribe [:db/get-item [:home :screen/screen-loaded?]])]
          [:<> [label]
               [breadcrumbs]
               ;[elements/horizontal-separator {:size :xxl}]
               [menu-groups]]
          [common/surface-box-layout-ghost-view :home.screen/view {:breadcrumb-count 1}]))

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
