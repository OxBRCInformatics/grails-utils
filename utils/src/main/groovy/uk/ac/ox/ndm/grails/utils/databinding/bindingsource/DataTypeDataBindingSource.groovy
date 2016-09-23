package uk.ac.ox.ndm.grails.utils.databinding.bindingsource

import uk.ac.ox.ndm.grails.utils.domain.DataType

/**
 * @since 16/10/2015
 */
class DataTypeDataBindingSource extends AbstractDomainDataBindingSource<DataType> {

    DataTypeDataBindingSource(Map map, Class<DataType> bindingTargetType) {
        super(map, bindingTargetType)
    }

    @Override
    DataType createDomain(Map map) {
        def domain = bindingTargetType.findById(map.key) ?:
                     bindingTargetType.findByIdIlike(map.key) ?:
                     bindingTargetType.findByLabel(map.key)
        bindingTargetType.findByLabelIlike(map.key)
        if (!domain) {
            domain = bindingTargetType.newInstance()
            domain.id = map.key
            domain.label = 'Not Supplied'
            domain.save()
        }
        domain
    }
}
