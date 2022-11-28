
(ns app.website-menus.frontend.viewer.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]
              [vector.api                  :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items-placeholder
  []
  [elements/label ::menu-items-placeholder
                  {:color            :highlight
                   :content          :no-items-to-show
                   :font-size        :xs
                   :horizontal-align :center
                   :indent           {:top :m}}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item-list-table
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-menus.viewer])
        menu-items       @(r/subscribe [:x.db/get-item [:website-menus :viewer/viewed-item :menu-items]])]
       (letfn [(f [{:keys [label link]}]
                  [{:content label :color :muted :selectable? true}
                   {:content link  :color :muted :selectable? true}])]
              [components/data-table ::menu-item-list-table
                                     {:disabled? viewer-disabled?
                                      :indent    {:left :s :top :m}
                                      :rows (vector/cons-item (vector/->items menu-items f)
                                                              [{:content :label}
                                                               {:content :link}])}])))

(defn- menu-item-list
  []
  (let [menu-items @(r/subscribe [:x.db/get-item [:website-menus :viewer/viewed-item :menu-items]])]
       (if (empty? menu-items)
           [menu-items-placeholder]
           [menu-item-list-table])))

(defn- menu-menu-items-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-menus.viewer])]
       [components/surface-box ::menu-menu-items-box
                               {:content [:<> [menu-item-list]
                                              [elements/horizontal-separator {:height :s}]]
                                :label     :menu-items
                                :disabled? viewer-disabled?}]))
