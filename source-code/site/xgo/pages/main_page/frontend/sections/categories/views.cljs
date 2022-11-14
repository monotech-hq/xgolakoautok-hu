
(ns site.xgo.pages.main-page.frontend.sections.categories.views
  (:require [re-frame.api :as r]
            [mid-fruits.normalize :as normalize]
            [site.xgo.pages.main-page.frontend.sections.categories.subs]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- category-button [[id {:category/keys [name] :as category}]]
  [:button {:key           id
            :class         "xgo-category--button"
            :on-click      #(r/dispatch [:categories/select! name])
            :data-selected @(r/subscribe [:categories/selected? name])}
     name])

(defn- categories [{:keys [categories]}]
  [:div {:id "xgo-categories"}
    (doall (map category-button categories))])

(defn- category-description [{:keys [description]}]
  [:div {:id "xgo-categories--description"}
    description])
;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn view []
  (let [view-props {:categories @(r/subscribe [:db/get-item [:site :categories]])
                    :description @(r/subscribe [:category/description])}]
       [:section {:id "xgo-categories--container"}
                 [categories view-props]
                 [category-description view-props]]))
