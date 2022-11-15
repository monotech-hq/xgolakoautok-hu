
(ns app.categories.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group      :vehicles
                                                            :icon       :list
                                                            :icon-color "#4a8bbf"
                                                            :label      :categories
                                                            :on-click   [:x.router/go-to! "/@app-home/categories"]
                                                            :horizontal-weight 0}]
                              [:home.sidebar/add-menu-item! {:group      :vehicles
                                                             :icon       :list
                                                             :icon-color "#4a8bbf"
                                                             :label      :categories
                                                             :on-click   [:x.router/go-to! "/@app-home/categories"]
                                                             :vertical-weight 0}]]}})
