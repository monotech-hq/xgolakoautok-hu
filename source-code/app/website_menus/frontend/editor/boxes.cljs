
(ns app.website-menus.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [random.api                  :as random]
              [re-frame.api                :as r]
              [vector.api                  :as vector]

              ; TEMP#0880 (source-code/app/common/frontend/item_lister/views.cljs)
              [plugins.dnd-kit.api :as dnd-kit]))

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

(defn- menu-item-link-field
  ; @param (integer) item-dex
  ; @param (map) item-props
  ; @param (map) drag-props
  [item-dex _ _]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-menus.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :indent      {:left :xs :top :xxs}
                             :placeholder :website-menu-item-link-placeholder
                             :value-path  [:website-menus :editor/edited-item :menu-items item-dex :link]}]))

(defn- menu-item-label-field
  ; @param (integer) item-dex
  ; @param (map) item-props
  ; @param (map) drag-props
  [item-dex _ _]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-menus.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :indent      {:left :xs :top :xxs}
                             :placeholder :website-menu-item-label-placeholder
                             :value-path  [:website-menus :editor/edited-item :menu-items item-dex :label]}]))

(defn- menu-item-drag-handle
  ; @param (integer) item-dex
  ; @param (map) item-props
  ; @param (map) drag-props
  ;  {:handle-attributes (map)}
  [_ _ {:keys [handle-attributes]}]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-menus.editor])]
       [:div handle-attributes [elements/icon {:disabled? editor-disabled?
                                               :icon      :drag_handle
                                               :indent    {:left :xs :top :xs}
                                               :style     {:cursor :grab}}]]))

(defn- remove-menu-item-button
  ; @param (integer) item-dex
  ; @param (map) item-props
  ; @param (map) drag-props
  [item-dex _ _]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-menus.editor])]
       [elements/icon-button {:color       :warning
                              :disabled?   editor-disabled?
                              :height      :l
                              :hover-color :highlight
                              :icon        :close
                              :indent      {:top :xxs}
                              :on-click    [:x.db/apply-item! [:website-menus :editor/edited-item :menu-items]
                                                              vector/remove-nth-item item-dex]
                              :tooltip     :remove-menu-item!}]))

(defn- menu-item
  ; @param (integer) item-dex
  ; @param (map) item-props
  ; @param (map) drag-props
  ;  {:item-attributes (map)}
  [item-dex item-props {:keys [item-attributes] :as drag-props}]
  [:div (update item-attributes :style merge {:display "flex"})
        [menu-item-drag-handle item-dex item-props drag-props]
        [:div (forms/form-row-attributes)
              [:div (forms/form-block-attributes {:ratio 30})
                    [menu-item-label-field item-dex item-props drag-props]]
              [:div (forms/form-block-attributes {:ratio 70})
                    [menu-item-link-field item-dex item-props drag-props]]]
        [remove-menu-item-button item-dex item-props drag-props]])

(defn- menu-item-list-body
  []
  (let [menu-items @(r/subscribe [:x.db/get-item [:website-menus :editor/edited-item :menu-items]])]
       [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "12px"}}
             [dnd-kit/body ::menu-item-list
                           {:items            menu-items
                            :item-id-f        :id
                            :item-element     [menu-item]
                            :on-order-changed (fn [_ _ %3] (r/dispatch-sync [:x.db/set-item! [:website-menus :editor/edited-item :menu-items] %3]))}]]))

(defn- menu-item-list-header
  []
  [:div {:style {:padding "24px 48px 0 36px"}}
        [:div (forms/form-row-attributes)
              [:div (forms/form-block-attributes {:ratio 30})
                    [elements/label {:color :muted :content :label :font-size :xs :indent {:left :xs}}]]
              [:div (forms/form-block-attributes {:ratio 70})
                    [elements/label {:color :muted :content :link  :font-size :xs :indent {:left :xs}}]]]])

(defn- menu-item-list
  []
  (let [menu-items @(r/subscribe [:x.db/get-item [:website-menus :editor/edited-item :menu-items]])]
       (if (empty? menu-items)
           [menu-items-placeholder]
           [:<> [menu-item-list-header]
                [menu-item-list-body]])))

(defn- add-menu-item-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-menus.editor])]
       (letfn [(f [%] (vector/conj-item % {:id (random/generate-string)}))]
              [:div {:style {:display "flex"}}
                    [elements/button ::add-menu-item-button
                                     {:color     :muted
                                      :disabled? editor-disabled?
                                      :font-size :xs
                                      :indent    {:vertical :s}
                                      :label     :add-menu-item!
                                      :on-click  [:x.db/apply-item! [:website-menus :editor/edited-item :menu-items] f]}]])))

(defn- menu-menu-items-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-menus.editor])]
       [components/surface-box ::menu-menu-items-box
                               {:content [:<> [add-menu-item-button]
                                              [menu-item-list]
                                              [elements/horizontal-separator {:height :s}]]
                                :label     :menu-items
                                :disabled? editor-disabled?}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-menus.editor])]
       [elements/combo-box ::menu-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:website-menus :editor/suggestions :name]
                            :placeholder  :website-menu-name-placeholder
                            :required?    true
                            :value-path   [:website-menus :editor/edited-item :name]}]))

(defn- menu-basic-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :website-menus.editor])]
       [components/surface-box ::menu-basic-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [menu-name-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :basic-data}]))
